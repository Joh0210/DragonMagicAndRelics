# Guide Template
This file provides Codesnippets for Guide Entries as well as a GPT-Prompt to help refactoring and formatting the entries.

## GPT
```
You will be provided with a small Item Description of a Minecraft-Mod-Item. The Job of yours is to Refactor the Item Description. You need to provide 3 segments of the description.

1. Fluff: This description segment is purely cosmetic. It will consist of an short in game sorry about the Item. For that you will imagine that you are a Wizzard and discovered/crafted the item, and you write down why you crafted it, how you discovered it or a small joke about the item. Sometimes you will be provided with additional Information for the fluff. For the tone be a slightly sarcastic wizard. You can be a bit silly but keep it slight. It should be 1–3 sentences
2. Description: This is the real description of the Item. You try to explain what the item really does.
3. Tooltip: A small Description (1 Sentence) what the item does. If the Description is short as well, the Tooltip can be the same
```

## Codesnippets

``` json
"TODO": {
    "index": "1",
    "category": "todo",
    "sections": [
      {
        "type": "title",
        "value": "TODO"
      },
      {
        "type": "text",
        "value": "",
        "json": [
          {
            "text": "TODO",
            "color": "",
            "italic": true
          },
          {
            "text": "TODO",
            "color": ""
          }
        ]
      }
    ],
    "related_recipes": [
      
    ]
  }
```

```json
  "item.dmnr.TODO.description" : "TODO",
```

## Factions:
```json
{
    "type": "text",
    "value": "",
    "json": [
      {
        "text": "This Crown can only be crafted by the \"",
        "color": ""
      },
      {
        "text": "Undead.",
        "color": "dark_gray"
      }
    ]
}

```

## Groups
### Artifice
```json
    "index": "1",
    "category": "artifice",
```

```json
{
    "type": "manaweaving_altar",
    "location":"dmnr:manaweaving/TODO"
}
```

``` json
"TODO": {
    "index": "1",
    "category": "artifice",
    "sections": [
      {
        "type": "title",
        "value": "TODO"
      },
      {
        "type": "text",
        "value": "",
        "json": [
          {
            "text": "TODO",
            "color": "",
            "italic": true
          },
          {
            "text": "TODO",
            "color": ""
          }
        ]
      }
    ],
    "related_recipes": [
      {
        "type": "manaweaving_altar",
        "location":"dmnr:manaweaving/TODO"
      }
    ]
  }
```

