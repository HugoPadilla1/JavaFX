package FinalExtraCredit;

import java.io.Serializable;

public class GameCharacter implements Serializable {
	private static final long serialVersionUID = 1L;
	private CharacterType type;
	private String name;
	private int hitPoints;
    private int armorClass;
    private int damage;
    private WeaponType weapon;
	private SpellType spell;

	private String specialAbility;

    public GameCharacter(){
		this.type = null;
		this.name = "";
		this.hitPoints = 0;
		this.armorClass = 0;
		this.damage = 0;
		this.weapon = null;
		this.specialAbility = "";
	}

	public GameCharacter(String name, CharacterType type, int hitPoints, int armorClass, int damage, WeaponType weapon, String specialAbility) {
		this.type = type;
		this.name = name;
		this.hitPoints = hitPoints;
		this.armorClass = armorClass;
		this.damage = damage;
		this.weapon = weapon;
		this.specialAbility = specialAbility;
	}

	public CharacterType getType() {
		return type;
	}

	public void setType(CharacterType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	public int getArmorClass() {
		return armorClass;
	}

	public void setArmorClass(int armorClass) {
		this.armorClass = armorClass;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public WeaponType getWeapon() {
		return weapon;
	}

	public void setWeapon(WeaponType weapon) {
		this.weapon = weapon;
	}

	public SpellType getSpell() {
		return spell;
	}

	public void setSpell(SpellType spell) {
		this.spell = spell;
	}
	public String getSpecialAbility() {
		return specialAbility;
	}

	public void setSpecialAbility(String specialAbility) {
		this.specialAbility = specialAbility;
	}

	@Override
	public String toString() {
		return "GameCharacter: Name -  " + name + " Type - " + type + " Hit Points - " + hitPoints +
				" Armor Class - " + armorClass + " Damage - " + damage + " Weapon - " + weapon +
				"\n Abilities\n" + specialAbility;
	}
}
