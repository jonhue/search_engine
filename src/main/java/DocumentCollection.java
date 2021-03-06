public class DocumentCollection {
  private DocumentCollectionCell head;
  private DocumentCollectionCell tail;

  public String toString() {
    return "<DocumentCollection size=" + numDocuments() + ">";
  }

  public boolean equals(DocumentCollection documentCollection) {
    if (documentCollection == null) return false;
    if (documentCollection.numDocuments() != numDocuments()) return false;

    boolean isEqual = true;
    for (int i = 0; i < numDocuments() && isEqual; ++i)
      if (!documentCollection.get(i).equals(get(i)))
        isEqual = false;

    return isEqual;
  }

  public void prependDocument(Document document) {
    if (document == null) return;

    head = new DocumentCollectionCell(document, head);
    if (head.getNext() == null)
      tail = head;
  }

  public void appendDocument(Document document) {
    if (document == null) return;

    if (isEmpty()) {
      head = tail = new DocumentCollectionCell(document);
    } else {
      tail.setNext(new DocumentCollectionCell(document));
      tail = tail.getNext();
    }
  }

  public boolean isEmpty() {
    return head == null && tail == null;
  }

  public int numDocuments() {
    if (isEmpty()) return 0;

    int num = 0;
    for (DocumentCollectionCell documentCollectionCell = head; documentCollectionCell != null; documentCollectionCell = documentCollectionCell.getNext())
      ++num;

    return num;
  }

  public Document getFirstDocument() {
    if (isEmpty()) return null;

    return get(0);
  }

  public Document getLastDocument() {
    if (isEmpty()) return null;

    return get(numDocuments() - 1);
  }

  public Document get(int index) {
    DocumentCollectionCell documentCollectionCell = getCell(index);
    if (documentCollectionCell == null) return null;

    return documentCollectionCell.getDocument();
  }

  public void removeFirstDocument() {
    remove(0);
  }

  public void removeLastDocument() {
    remove(numDocuments() - 1);
  }

  public boolean remove(int index) {
    if (index == 0) {
      if (head == null) return false;

      head = head.getNext();
      if (head == null)
        tail = null;
      else
        head.setPrevious(null);
    } else {
      DocumentCollectionCell documentCollectionCellTail = getCell(index - 1);
      DocumentCollectionCell documentCollectionCellHead = getCell(index + 1);
      if (documentCollectionCellTail == null) return false;

      documentCollectionCellTail.setNext(documentCollectionCellHead);

      if (documentCollectionCellHead == null)
        tail = documentCollectionCellTail;
    }

    return true;
  }

  public int indexOf(Document document) {
    if (document == null) return -1;
    if (isEmpty()) return -1;

    int index = 0;
    for (DocumentCollectionCell documentCollectionCell = head; documentCollectionCell != null && !documentCollectionCell.getDocument().equals(document); documentCollectionCell = documentCollectionCell.getNext())
      ++index;

    if (index == numDocuments())
      return -1;
    else
      return index;
  }

  public boolean contains(Document document) {
    return indexOf(document) != -1;
  }

  public void match(String query) {
    prependDocument(new Document("query", "", "", new Date(), new Author("", "", new Date(), "", "a@b.de"), query));

    match();
  }

  protected void match() {
    addZeroWordsToDocuments();
    for (DocumentCollectionCell documentCollectionCell = head; documentCollectionCell != null; documentCollectionCell = documentCollectionCell.getNext()) {
      documentCollectionCell.getDocument().getWordCounts().sort();
      documentCollectionCell.setSimilarity(documentCollectionCell.getDocument().getWordCounts().computeSimilarity(getFirstDocument().getWordCounts()));
    }

    head = head.getNext();
    sortBySimilarityDesc();
  }

  public double getQuerySimilarity(int index) {
    DocumentCollectionCell documentCollectionCell = getCell(index);
    if (documentCollectionCell == null) return -1.0;

    return documentCollectionCell.getSimilarity();
  }

  public int noOfDocumentsContainingWord(String word) {
    if (isEmpty()) return 0;

    int num = 0;
    for (int i = 0; i < numDocuments(); ++i)
      if (get(i).getWordCounts().getCount(get(i).getWordCounts().getIndexOfWord(word)) > 0)
        ++num;

    return num;
  }

  protected DocumentCollectionCell getCell(int index) {
    if (index < 0 || index >= numDocuments()) return null;

    DocumentCollectionCell documentCollectionCell = head;
    for (int i = 0; i < index; ++i)
      documentCollectionCell = documentCollectionCell.getNext();

    return documentCollectionCell;
  }

  private WordCountsArray allWords() {
    WordCountsArray wordCountsArray = new WordCountsArray(100);

    for (DocumentCollectionCell documentCollectionCell = head; documentCollectionCell != null; documentCollectionCell = documentCollectionCell.getNext())
      for (int i = 0; i < documentCollectionCell.getDocument().getWordCounts().size(); ++i)
        wordCountsArray.add(documentCollectionCell.getDocument().getWordCounts().getWord(i), 0);

    return wordCountsArray;
  }

  private void addZeroWordsToDocuments() {
    for (DocumentCollectionCell documentCollectionCell = head; documentCollectionCell != null; documentCollectionCell = documentCollectionCell.getNext())
      for (int i = 0; i < allWords().size(); ++i)
        documentCollectionCell.getDocument().getWordCounts().add(allWords().getWord(i), 0);
  }

  private void sortBySimilarityDesc() {
    // Build array from list & reset list elements
    DocumentCollectionCell[] arr = toArray();

    // Sort array
    MergeSort.sort(arr);

    // Build new list
    fromArray(arr);
  }

  protected DocumentCollectionCell[] toArray() {
    DocumentCollectionCell[] result = new DocumentCollectionCell[numDocuments()];
    for (int i = 0; i < result.length; i++)
      result[i] = getCell(i);

    for (DocumentCollectionCell cell : result) {
      cell.setNext(null);
      cell.setPrevious(null);
    }

    return result;
  }

  protected void fromArray(DocumentCollectionCell[] arr) {
    for (int i = 1; i < arr.length; i++)
      arr[i].setPrevious(arr[i - 1]);
    head = arr[0];
    tail = arr[arr.length - 1];
  }
}

class MergeSort {
  public static void sort(DocumentCollectionCell[] arr) {
    for (int maxSortedPartLength = 1; maxSortedPartLength < arr.length; maxSortedPartLength *= 2) {
      int startPos = 0;
      while (startPos < arr.length) {
        int endPos = endPos(startPos, maxSortedPartLength, arr.length);
        java.lang.System.arraycopy(merge(arr, startPos, dividePos(startPos, maxSortedPartLength, arr.length), endPos), 0, arr, startPos, endPos - startPos);
        startPos = endPos;
      }
    }
  }

  private static DocumentCollectionCell[] merge(DocumentCollectionCell[] arr, int startPos, int dividePos, int endPos) {
    DocumentCollectionCell[] b = new DocumentCollectionCell[endPos - startPos];
    int k = 0;

    int i = startPos;
    int j = dividePos;
    while (k < endPos) {
      if (i == dividePos) {
        java.lang.System.arraycopy(arr, j, b, k, endPos - j);
        k = endPos;
      } else if (j == endPos) {
        java.lang.System.arraycopy(arr, i, b, k, dividePos - i);
        k = endPos;
      } else {
        if (arr[i].getSimilarity() > arr[j].getSimilarity())
          b[k++] = arr[i++];
        else
          b[k++] = arr[j++];
      }
    }

    return b;
  }

  private static int dividePos(int startPos, int maxSortedPartLength, int arrLength) {
    if (startPos + maxSortedPartLength >= arrLength)
      return arrLength;
    else if (startPos + 2 * maxSortedPartLength >= arrLength)
      return startPos + maxSortedPartLength;
    else
      return startPos + maxSortedPartLength;
  }

  private static int endPos(int startPos, int maxSortedPartLength, int arrLength) {
    if (startPos + 2 * maxSortedPartLength >= arrLength)
      return arrLength;
    else
      return startPos + 2 * maxSortedPartLength;
  }
}