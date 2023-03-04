package technology.rocketjump.civblitz.model;

public class Patch {
	protected String sqlTemplate;
	protected boolean needsDedicatedAbility = false;

	public String getSqlTemplate() {
		return sqlTemplate;
	}

	public void setSqlTemplate(String sqlTemplate) {
		this.sqlTemplate = sqlTemplate;
	}

	public boolean needsDedicatedAbility() {
		return needsDedicatedAbility;
	}

	public void setNeedsDedicatedAbility(boolean needsDedicatedAbility) {
		this.needsDedicatedAbility = needsDedicatedAbility;
	}
}
