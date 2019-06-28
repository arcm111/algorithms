public class Array<T> {
	private Object[] values;

	public Array(int size) {
		values = new Object[size];
	}

	@SuppressWarnings("unchecked")
	public T get(int index) {
		return (T) values[index];
	}

	@SuppressWarnings("unchecked")
	public void set(int index, T value) {
		values[index] = value;
	}

	public int length() {
		return values.length;
	}
}
