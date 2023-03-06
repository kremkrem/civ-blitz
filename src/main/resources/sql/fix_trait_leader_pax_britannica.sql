-- Include Harbors and (Phoenician) Cothons as districts valid for a free naval unit.
INSERT INTO Modifiers (ModifierId, ModifierType)
VALUES
    ('TRAIT_HARBOR_NAVAL_UNIT', 'MODIFIER_PLAYER_ADJUST_DISTRICT_ADD_NAVAL_UNIT'),
    ('TRAIT_COTHON_NAVAL_UNIT', 'MODIFIER_PLAYER_ADJUST_DISTRICT_ADD_NAVAL_UNIT');
INSERT INTO ModifierArguments (ModifierId, Name, Value)
VALUES
    ('TRAIT_HARBOR_NAVAL_UNIT', 'DistrictType', 'DISTRICT_HARBOR'),
    ('TRAIT_COTHON_NAVAL_UNIT', 'DistrictType', 'DISTRICT_COTHON');
INSERT INTO TraitModifiers (TraitType, ModifierId)
VALUES
    ('TRAIT_LEADER_PAX_BRITANNICA', 'TRAIT_HARBOR_NAVAL_UNIT'),
    ('TRAIT_LEADER_PAX_BRITANNICA', 'TRAIT_COTHON_NAVAL_UNIT');