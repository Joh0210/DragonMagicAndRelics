package de.joh.dmnr.compat.ae2.logic;

import appeng.api.networking.crafting.CalculationStrategy;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.networking.crafting.ICraftingService;
import appeng.api.networking.crafting.ICraftingSubmitResult;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.storage.StorageHelper;
import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * Keeps track of the autocrafting jobs one {@link ICraftingRequester} has requested, one job per item.
 * <p>
 * A job goes through two stages: first the crafting plan is calculated, which AE2 does asynchronously, then the plan
 * is submitted and the {@link ICraftingLink} to the running job is kept until AE2 reports that it ended
 * (see {@link #jobStateChange}).
 * @author Joh0210
 */
public class AltarCraftingTracker {
    private static final String TAG_JOBS = "craftingJobs";
    private static final String TAG_KEY = "key";
    private static final String TAG_LINK = "link";

    private static final int RETRY_DELAY_TICKS = 100;

    private final ICraftingRequester owner;
    private final Map<AEKey, Job> jobs = new LinkedHashMap<>();
    /** Game time before which a failed calculation is not repeated, so items that can not be crafted do not cost a calculation every cycle. */
    private final Map<AEKey, Long> retryAt = new HashMap<>();

    private static class Job {
        Future<ICraftingPlan> calculation;
        ICraftingLink link;
    }

    public AltarCraftingTracker(@NotNull ICraftingRequester owner) {
        this.owner = owner;
    }

    /**
     * @return an item matching the filter for which a calculation is running or a crafting job is under way
     */
    public @Nullable AEKey findBusy(@NotNull Predicate<AEKey> filter) {
        for (AEKey key : jobs.keySet()) {
            if (filter.test(key)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Starts crafting the item, or advances the stage of the job that is already requested for it.
     * @return true if a crafting job was submitted
     */
    public boolean handleCrafting(@NotNull AEKey what, long amount, @NotNull Level level, @NotNull ICraftingService crafting, @NotNull IActionSource source) {
        Job job = jobs.get(what);

        if (job == null) {
            Long retry = retryAt.get(what);
            if (retry != null) {
                if (level.getGameTime() < retry) {
                    return false;
                }
                retryAt.remove(what);
            }
            job = new Job();
            job.calculation = crafting.beginCraftingCalculation(level, () -> source, what, amount, CalculationStrategy.REPORT_MISSING_ITEMS);
            jobs.put(what, job);
            return false;
        }

        if (job.link != null || !job.calculation.isDone()) {
            return false;
        }

        try {
            ICraftingPlan plan = job.calculation.get();
            // A simulation is a plan that lacks ingredients: it can not be crafted (yet).
            if (!plan.simulation()) {
                ICraftingSubmitResult result = crafting.submitJob(plan, owner, null, false, source);
                if (result.successful() && result.link() != null) {
                    job.link = result.link();
                    return true;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException | CancellationException e) {
            // the calculation failed, ask again the next time the item is missing
        }

        jobs.remove(what);
        retryAt.put(what, level.getGameTime() + RETRY_DELAY_TICKS);
        return false;
    }

    public @NotNull ImmutableSet<ICraftingLink> getRequestedJobs() {
        ImmutableSet.Builder<ICraftingLink> links = ImmutableSet.builder();
        for (Job job : jobs.values()) {
            if (job.link != null) {
                links.add(job.link);
            }
        }
        return links.build();
    }

    /**
     * Called by AE2 when the state of a crafting job changed (finished or canceled).
     */
    public void jobStateChange(@NotNull ICraftingLink link) {
        jobs.values().removeIf(job -> job.link != null && job.link.getCraftingID().equals(link.getCraftingID()));
    }

    public void cancel() {
        // Canceling a link makes AE2 call jobStateChange, which removes from the map, so it must not be iterated.
        List<Job> canceled = new ArrayList<>(jobs.values());
        jobs.clear();
        for (Job job : canceled) {
            if (job.link != null) {
                job.link.cancel();
            }
            if (job.calculation != null) {
                job.calculation.cancel(true);
            }
        }
    }

    public void writeToNBT(@NotNull CompoundTag tag) {
        ListTag list = new ListTag();
        for (Map.Entry<AEKey, Job> entry : jobs.entrySet()) {
            ICraftingLink link = entry.getValue().link;
            if (link == null) {
                continue;
            }

            CompoundTag jobTag = new CompoundTag();
            jobTag.put(TAG_KEY, entry.getKey().toTagGeneric());
            CompoundTag linkTag = new CompoundTag();
            link.writeToNBT(linkTag);
            jobTag.put(TAG_LINK, linkTag);
            list.add(jobTag);
        }

        if (list.isEmpty()) {
            tag.remove(TAG_JOBS);
        } else {
            tag.put(TAG_JOBS, list);
        }
    }

    public void readFromNBT(@NotNull CompoundTag tag) {
        jobs.clear();
        for (Tag element : tag.getList(TAG_JOBS, Tag.TAG_COMPOUND)) {
            CompoundTag jobTag = (CompoundTag) element;
            AEKey key = AEKey.fromTagGeneric(jobTag.getCompound(TAG_KEY));
            ICraftingLink link = StorageHelper.loadCraftingLink(jobTag.getCompound(TAG_LINK), owner);
            if (key != null && link != null) {
                Job job = new Job();
                job.link = link;
                jobs.put(key, job);
            }
        }
    }

    /**
     * Forgets jobs whose link ended without AE2 having reported it.
     */
    public void removeEndedJobs() {
        jobs.values().removeIf(job -> job.link != null && (job.link.isDone() || job.link.isCanceled()));
    }
}
