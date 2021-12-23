case class pdfObject(
    name: String = "N/A",
    source: String = "N/A",
    driveLink: String = "N/A",
    genre: String = "N/A",
    tags: List[String] = List(),
    pageNumbers: Int = 0,
    rating: String = "N/A",
    read: Boolean = false,
    favourite: Boolean = false,
    extraMaterial: String = "N/A",
    category: Category = Category.Uncategorized,
    categoryInfo: List[String] = List(), // BUILDER PATTERN?
    rpg: String = "System Neutral",
    description: String = "N/A"
):

  def categorySpecifics: String =
    if (category.header == List()) {
      ""
    } else {
      val zippedSingle = category.header
        .zip(categoryInfo)
        .map { case (header, info) =>
          s"$header: $info"
        }
        .mkString(", ")
      s"\n| $zippedSingle"
    }

  def categoryName: String = category.title

  def getWinPdf: String = source.replace("/", "\\") ++ ".pdf"

  override def toString =
    s"$name - $categoryName\n" +
      s"| $rpg\n" +
      s"| $rpg\n" +
      s"| Source: $getWinPdf\n" +
      s"| drivelink: $driveLink\n" +
      s"| Genre: $genre, Tags: ${tags.mkString(", ")}\n" +
      s"| Pages: $pageNumbers, Rating: $rating\n" +
      s"| Read: $read, Favourite: $favourite\n" +
      s"| Extra material: $extraMaterial" +
      s"$categorySpecifics";
  def toStringSmall =
    s"$name - $categoryName\n"

enum Category(
    val title: String,
    val header: List[String]
): // less hardcoded categories?
  case CollsCampaigns
      extends Category(
        "Adventure Collections and Campaigns",
        List("Amount", "Levelrange")
      )
  case Adventures extends Category("Adventures", List("Levelrange"))
  case AdventureLeague
      extends Category("Adventurers League", List("Est. Time", "Levelrange"))
  case AdventureSolo extends Category("Adventure - Solo", List("Levelrange"))
  case CharsRoleplay
      extends Category("Character Options and Roleplaying", List())
  case DMing extends Category("DMing", List())
  case EncountersQuest
      extends Category("Encounters and Quests", List("Amount", "levelrange"))
  case GearItemsSpells
      extends Category("Gear, Items, and Spells", List("Amount"))
  case GeneratorsBuilders extends Category("Generators and Builders", List())
  case ListsTables extends Category("Lists and Tables", List("Amount"))
  case LocationsGuides
      extends Category("Locations, Guides, and Gazatteers", List())
  case ModuleSupplments extends Category("Module Supplements", List("Module"))
  case MonstersCreatures
      extends Category("Monsters and Creatures", List("Amount"))
  case NPCsGroups extends Category("NPCs and Groups", List("Amount"))
  case RulesSystems extends Category("Rules and System(changes)", List())
  case SettingsMaterial
      extends Category("Settings and setting specific material", List())
  case SheetsCards extends Category("Sheets and Cards", List())
  case Supplements extends Category("Supplements", List())
  case Tools extends Category("Tools", List())
  case TrapsPuzzles extends Category("Traps, Puzzles, etc", List("Amount"))
  case Worldbuilding extends Category("Worldbuilding", List())
  case Uncategorized extends Category("Uncategorized", List())
