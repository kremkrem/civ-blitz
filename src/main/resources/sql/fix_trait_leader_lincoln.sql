-- PLANTATION_NEGATIVE_LOYALTY (-2 loyalty per plantation) relies on REQUIRES_PLAYER_IS_LINCOLN requirement set.
-- Add <modName> as another Civ that satisfies this requirement.
INSERT OR REPLACE INTO Requirements (RequirementId, RequirementType)
VALUES ('LINCOLN_<modName>_REQUIREMENTS', 'REQUIREMENT_PLAYER_LEADER_TYPE_MATCHES');

INSERT OR REPLACE INTO RequirementArguments (RequirementId, Name, Value)
VALUES ('LINCOLN_<modName>_REQUIREMENTS', 'LeaderType', 'LEADER_IMP_<modName>');

UPDATE RequirementSets
SET RequirementSetType = 'REQUIREMENTSET_TEST_ANY'
WHERE RequirementSetId = 'REQUIRES_PLAYER_IS_LINCOLN';

INSERT OR REPLACE INTO RequirementSetRequirements (RequirementSetId, RequirementId)
VALUES ('REQUIRES_PLAYER_IS_LINCOLN', 'LINCOLN_<modName>_REQUIREMENTS');