
public class TestKey implements Executable<Key> {
    public boolean execute(Key key1, Key key2) {
        return (key1.key < key2.key);
    }
}
