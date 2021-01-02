class Trie {
    Trie[] children;
    boolean isTail;

    Trie() {
        children = new Trie[26];
    }

    public Trie getNext(char c) {
        return children[c - 'A'];
    }

    public void addWord(String s) {
        Trie curr = this;
        for (int i = 0; i < s.length(); i++) {
            if (curr.children[s.charAt(i) - 'A'] == null) {
                curr.children[s.charAt(i) - 'A'] = new Trie();
            }
            curr = curr.children[s.charAt(i) - 'A'];
        }
        curr.isTail = true;
    }

    public boolean isWord(String s) {
        Trie curr = this;
        for (int i = 0; i < s.length(); i++) {
            if (curr.children[s.charAt(i) - 'A'] == null) {
                return false;
            }
            curr = curr.children[s.charAt(i) - 'A'];
        }
        return curr.isTail;
    }
}