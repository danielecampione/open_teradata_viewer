final def removeDuplicates = true // Change to "false" if you want to keep duplicates

// Note: You'll want to consider wrapping your scripts inside calls to
// beginAtomicEdit() and endAtomicEdit(), so the actions they perform can
// be undone with a single Undo action.
textArea.beginAtomicEdit()
try {
    def lines = textArea.text.split("\n")

    if (removeDuplicates) {
        def ts = new TreeSet()
        lines.each {
            ts.add(it)
        }
        lines = ts.toArray()
    }

    Arrays.sort(lines)
    textArea.text = lines.join("\n")
} finally {
    textArea.endAtomicEdit()
}