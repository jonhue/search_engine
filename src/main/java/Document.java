public class Document {
  public static final String[] SUFFICES = {"fikation", "fizieren", "isierung", "gerecht", "isieren", "haltig", "schaft",
          "würdig", "artig", "fähig", "gemäß", "ieren", "ismus", "kunde", "logie", "legen", "meter", "chen", "fach",
          "haft", "heit", "iren", "isch", "ität", "keit", "lein", "lich", "ling", "voll", "wert", "ant", "bar", "eln",
          "end", "ent", "ion", "ist", "los", "mal", "mut", "nis", "sam", "tum", "ung", "ab", "al", "ei", "en", "er",
          "ie", "ig", "in", "iv", "or"};
  private String title;
  private String lang;
  private String summary;
  private Date releaseDate;
  private Author author;
  private WordCountsArray wordCounts;

  public Document(String title, String lang, String summary, Date releaseDate, Author author, String content) throws IllegalArgumentException {
    wordCounts = new WordCountsArray(0);

    setTitle(title);
    setLang(lang);
    setSummary(summary);
    setReleaseDate(releaseDate);
    setAuthor(author);
    addContent(content);
  }

  private static String[] tokenize(String content) {
    int wordCount = countSubstring(content, " ") + 1;
    String[] words = new String[wordCount];

    for (int i = 0; substringPos(content, " ") != -1; ++i) {
      words[i] = substring(content, 0, substringPos(content, " "));
      content = substring(content, words[i].length() + 1, content.length());
    }
    words[wordCount - 1] = content;

    return words;
  }

  private static boolean sufficesEqual(String str1, String str2, int n) throws IllegalArgumentException {
    if (n > str1.length() || n > str2.length())
      throw new IllegalArgumentException("n must not be higher than the length of a passed string.");

    String sub1 = substring(str1, str1.length() - n, str1.length());
    String sub2 = substring(str2, str2.length() - n, str2.length());
    return sub1.equals(sub2);
  }

  private static String findSuffix(String word) throws IllegalArgumentException {
    for (String SUFFICE : SUFFICES) {
      int sufficeLength = SUFFICE.length();
      if (word.length() >= sufficeLength && sufficesEqual(word, SUFFICE, sufficeLength)) return SUFFICE;
    }

    return "";
  }

  private static String cutSuffix(String word, String suffix) throws IllegalArgumentException {
    if (sufficesEqual(word, suffix, suffix.length()))
      return substring(word, 0, word.length() - suffix.length());

    return word;
  }

  private static int substringPos(String haystack, String needle) {
    int pos = -1;
    for (int i = 0; i < haystack.length() && pos == -1; ++i)
      if (isSubstringAtPos(haystack, needle, i))
        pos = i;

    return pos;
  }

  private static int countSubstring(String haystack, String needle) {
    int count = 0;
    for (int i = 0; i < haystack.length(); ++i)
      if (isSubstringAtPos(haystack, needle, i)) count = count + 1;

    return count;
  }

  private static boolean isSubstringAtPos(String haystack, String needle, int pos) {
    boolean isSubstringAtPos = true;
    for (int i = 0; i < needle.length() && isSubstringAtPos; ++i)
      if (haystack.length() - 1 < pos + i || haystack.charAt(pos + i) != needle.charAt(i))
        isSubstringAtPos = false;

    return isSubstringAtPos;
  }

  private static String substring(String str, int startPos, int endPos) {
    String newStr = "";
    for (int i = startPos; i < endPos; ++i)
      newStr = newStr + str.charAt(i);

    return newStr;
  }

  public String toString() {
    return "<Document title=" + title + " lang=" + lang + " author=" + author.toString() + ">";
  }

  public boolean equals(Document document) {
    if (document == null) return false;

    return document.title.equals(title) && document.lang.equals(lang) && document.summary.equals(summary) && document.releaseDate.equals(releaseDate) && document.author.equals(author) && document.wordCounts.equals(wordCounts);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Date getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public WordCountsArray getWordCounts() {
    return wordCounts;
  }

  public int getAgeAt(Date date) {
    return releaseDate.getAgeInDaysAt(date);
  }

  private void addContent(String content) throws IllegalArgumentException {
    String[] words = tokenize(content);

    for (int i = 0; i < words.length; ++i) {
      String suffix = findSuffix(words[i]);
      if (!suffix.equals(""))
        words[i] = cutSuffix(words[i], suffix);
      wordCounts.add(words[i], 1);
    }
  }
}
