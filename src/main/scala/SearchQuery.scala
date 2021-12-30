case class SearchQuery(
    name: String = "",
    genre: String = "",
    tags: TagList = new TagList,
    pageNumbers: Int = 0,
    rating: String = "",
    read: Boolean = false,
    favourite: Boolean = false,
    category: Category = Category.Uncategorized,
    rpg: String = ""
):
  def tagsInString = tags.pQueue.mkString("\n")
