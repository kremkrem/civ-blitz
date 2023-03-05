-- Make amenities and faith generation in Aqueducts also applicable to (Roman) Baths.
UPDATE RequirementSets
SET RequirementSetType='REQUIREMENTSET_TEST_ANY'
WHERE RequirementSetId IN ('DISTRICT_IS_AQUEDUCT_FAITH', 'DISTRICT_IS_AQUEDUCT_AMENITY');

INSERT OR REPLACE INTO RequirementSetRequirements (RequirementSetId, RequirementId)
VALUES
    ('DISTRICT_IS_AQUEDUCT_FAITH', 'REQUIRES_DISTRICT_IS_BATH'),
    ('DISTRICT_IS_AQUEDUCT_AMENITY', 'REQUIRES_DISTRICT_IS_BATH');
-- Add +2 food to farms adjacent to (Roman) Baths.
INSERT OR REPLACE INTO Requirements (RequirementId, RequirementType)
VALUES ('REQUIRES_PLOT_ADJACENT_TO_BATH_BARAYS_<modName>', 'REQUIREMENT_PLOT_ADJACENT_DISTRICT_TYPE_MATCHES');
INSERT OR REPLACE INTO RequirementArguments (RequirementId, Name, Value)
VALUES ('REQUIRES_PLOT_ADJACENT_TO_BATH_BARAYS_<modName>', 'DistrictType', 'DISTRICT_BATH');

INSERT OR REPLACE INTO RequirementSets (RequirementSetId, RequirementSetType)
VALUES ('PLOT_IS_FARM_BATH_ADJACENT_BARAYS_<modName>_REQUIRMENTS', 'REQUIREMENTSET_TEST_ALL');
INSERT OR REPLACE INTO RequirementSetRequirements (RequirementSetId, RequirementId)
VALUES
    ('PLOT_IS_FARM_BATH_ADJACENT_BARAYS_<modName>_REQUIRMENTS', 'REQUIRES_PLOT_HAS_FARM'),
    ('PLOT_IS_FARM_BATH_ADJACENT_BARAYS_<modName>_REQUIRMENTS', 'REQUIRES_PLOT_ADJACENT_TO_BATH_BARAYS_<modName>');

INSERT OR REPLACE INTO Modifiers (ModifierId, ModifierType, SubjectRequirementSetId)
VALUES (
    'TRAIT_FARM_BATH_ADJECENCY_FOOD_BARAYS_<modName>',
    'MODIFIER_PLAYER_ADJUST_PLOT_YIELD',
    'PLOT_IS_FARM_BATH_ADJACENT_BARAYS_<modName>_REQUIRMENTS');
INSERT OR REPLACE INTO ModifierArguments (ModifierId, Name, Value)
VALUES
    ('TRAIT_FARM_BATH_ADJECENCY_FOOD_BARAYS_<modName>', 'YieldType', 'YIELD_FOOD'),
    ('TRAIT_FARM_BATH_ADJECENCY_FOOD_BARAYS_<modName>', 'Amount', '2');
INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId)
VALUES ('TRAIT_CIVILIZATION_<modName>','TRAIT_FARM_BATH_ADJECENCY_FOOD_BARAYS_<modName>');

-- Add +1 faith to farms adjacent to (Russian) Lavras.
INSERT OR REPLACE INTO Requirements (RequirementId, RequirementType)
VALUES ('REQUIRES_PLOT_ADJACENT_TO_LAVRA_BARAYS_<modName>', 'REQUIREMENT_PLOT_ADJACENT_DISTRICT_TYPE_MATCHES');
INSERT OR REPLACE INTO RequirementArguments (RequirementId, Name, Value)
VALUES ('REQUIRES_PLOT_ADJACENT_TO_LAVRA_BARAYS_<modName>', 'DistrictType', 'DISTRICT_LAVRA');

INSERT OR REPLACE INTO RequirementSets (RequirementSetId, RequirementSetType)
VALUES ('PLOT_IS_FARM_LAVRA_ADJACENT_BARAYS_<modName>_REQUIRMENTS', 'REQUIREMENTSET_TEST_ALL');
INSERT OR REPLACE INTO RequirementSetRequirements (RequirementSetId, RequirementId)
VALUES
    ('PLOT_IS_FARM_LAVRA_ADJACENT_BARAYS_<modName>_REQUIRMENTS', 'REQUIRES_PLOT_HAS_FARM'),
    ('PLOT_IS_FARM_LAVRA_ADJACENT_BARAYS_<modName>_REQUIRMENTS', 'REQUIRES_PLOT_ADJACENT_TO_LAVRA_BARAYS_<modName>');

INSERT OR REPLACE INTO Modifiers (ModifierId, ModifierType, SubjectRequirementSetId)
VALUES (
    'TRAIT_FARM_LAVRA_ADJECENCY_FAITH_BARAYS_<modName>',
    'MODIFIER_PLAYER_ADJUST_PLOT_YIELD',
    'PLOT_IS_FARM_LAVRA_ADJACENT_BARAYS_<modName>_REQUIRMENTS');
INSERT OR REPLACE INTO ModifierArguments (ModifierId, Name, Value)
VALUES
    ('TRAIT_FARM_LAVRA_ADJECENCY_FAITH_BARAYS_<modName>', 'YieldType', 'YIELD_FAITH'),
    ('TRAIT_FARM_LAVRA_ADJECENCY_FAITH_BARAYS_<modName>', 'Amount', '1');
INSERT OR REPLACE INTO TraitModifiers (TraitType, ModifierId)
VALUES ('TRAIT_CIVILIZATION_<modName>','TRAIT_FARM_LAVRA_ADJECENCY_FAITH_BARAYS_<modName>');