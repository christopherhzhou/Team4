import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JComponent;

/**
 * NOTES:
 * not sure what dim does but the
 * actual height of the map is 24
 * actual width of the map is 25
 */
public class Map {

	public enum Type {
		EMPTY,
		PACMAN,
		GHOST,
		WALL,
		COOKIE
	}

	private HashMap<Location, HashSet<Type>> field;
	private boolean gameOver;
	private int dim;

	private HashMap<String, Location> locations;
	private HashMap<String, JComponent> components;
	private HashSet<Type> emptySet;
	private HashSet<Type> wallSet;

	private int cookies = 0;

	public Map(int dim) {
		gameOver = false;
		locations = new HashMap<String, Location>();
		components = new HashMap<String, JComponent>();
		field = new HashMap<Location, HashSet<Type>>();

		emptySet = new HashSet<Type>();
		wallSet = new HashSet<Type>();
		emptySet.add(Type.EMPTY);
		wallSet.add(Type.WALL);
		this.dim = dim;
	}

	public void add(String name, Location loc, JComponent comp, Type type) {
		locations.put(name, loc);
		components.put(name, comp);
		if (!field.containsKey(loc))
			field.put(loc, new HashSet<Type>());
		field.get(loc).add(type);
	}

	public int getCookies() {
		return cookies;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean move(String name, Location loc, Type type) {
		// update locations, components, and field
		// use the setLocation method for the component to move it to the new location
		if (!locations.containsKey(name) || !field.containsKey(loc) || !components.containsKey(name)) {
			return false;
		}
		Location prevLoc = locations.get(name);
		components.get(name).setLocation(loc.x, loc.y);
		locations.replace(name, loc);
		field.get(prevLoc).remove(type);
		field.get(loc).add(type);
		return true;
	}

	/**
	 * If a location is on the map is will be in field.
	 * If a location is outside of the map is should return null;
	 * This way we can differentiate between Empty valid Location
	 * and outside of map.
	 * 
	 * @param loc
	 * @return
	 */
	public HashSet<Type> getLoc(Location loc) {
		// wallSet and emptySet will help you write this method
		if (!field.containsKey(loc)) {
			return wallSet;
		}
		return this.field.get(loc);
	}

	public boolean attack(String Name) {
		// update gameOver
		// checks if pacman is adjacent to the ghost
		Location loc_ghost = locations.get(Name);
		Location loc_pacman = locations.get("pacman");

		if (Math.abs(loc_ghost.x - loc_pacman.x) + Math.abs(loc_ghost.y - loc_pacman.y) == 1) {
			gameOver = true;
			return true;
		}

		return false;
	}

	public JComponent eatCookie(String name) {
		// System.out.println("eating cookie");
		// update locations, components, field, and cookies
		// the id for a cookie at (10, 1) is tok_x10_y1
		Location loc = locations.get(name);
		if (field.get(loc).contains(Map.Type.COOKIE)) {
			String cookieKey = "tok_x" + loc.x + "_y" + loc.y;
			cookies += 1;
			field.get(loc).remove(Map.Type.COOKIE);
			JComponent cookieComp = components.get(cookieKey);
			components.remove(cookieKey);
			return cookieComp;
		}
		return null;
	}
}