-- Add a new requirement ("leader has to have Dido's passive") to PROJECT_COTHON_MOVE_CAPITAL.
-- Otherwise, Cothon grants the project but completing it yields no results unless leader is Dido.
UPDATE Projects SET UnlocksFromEffect = 1
WHERE ProjectType = 'PROJECT_COTHON_CAPITAL_MOVE';
INSERT OR REPLACE INTO Types (Type, Kind)
VALUES ('MODIFIER_PLAYER_ALLOW_PROJECT_DIDO', 'KIND_MODIFIER');
INSERT OR REPLACE INTO DynamicModifiers (ModifierType, CollectionType, EffectType)
VALUES ('MODIFIER_PLAYER_ALLOW_PROJECT_DIDO', 'COLLECTION_OWNER', 'EFFECT_ADD_PLAYER_PROJECT_AVAILABILITY');

INSERT OR REPLACE INTO Modifiers (ModifierId, ModifierType)
VALUES ('CIV_IMP_ALLOW_COTHON_PROJECT', 'MODIFIER_PLAYER_ALLOW_PROJECT_DIDO');
INSERT OR REPLACE INTO ModifierArguments (ModifierId, Name, Value)
VALUES ('CIV_IMP_ALLOW_COTHON_PROJECT', 'ProjectType', 'PROJECT_COTHON_CAPITAL_MOVE');

INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId)
VALUES ('TRAIT_LEADER_FOUNDER_CARTHAGE', 'CIV_IMP_ALLOW_COTHON_PROJECT');