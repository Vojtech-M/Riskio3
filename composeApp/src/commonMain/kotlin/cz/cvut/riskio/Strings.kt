package cz.cvut.riskio

enum class Language { EN, CZ }

class Strings(val lang: Language) {
    val appTitle = if (lang == Language.EN) "RISK.IO REWARDS" else "RISK.IO ODMĚNY"
    val home = if (lang == Language.EN) "Home" else "Domů"
    val gamba = if (lang == Language.EN) "Gamba" else "Gamba"
    val todo = if (lang == Language.EN) "Tasks" else "Úkoly"
    val setupPool = if (lang == Language.EN) "Gamba Pool Setup" else "Nastavení výher"
    val rewardPlaceholder = if (lang == Language.EN) "Reward name..." else "Název odměny..."
    val add = if (lang == Language.EN) "Add" else "Přidat"
    val itemsInPool = if (lang == Language.EN) "Items in Pool:" else "Položky v osudí:"
    val currentReward = if (lang == Language.EN) "Current Reward" else "Aktuální odměna"
    val latestWin = if (lang == Language.EN) "LATEST WIN" else "POSLEDNÍ VÝHRA"
    val noWinYet = if (lang == Language.EN) "Win something in Gamba to see it here!" else "Vyhraj něco v Gambě, aby se to zde ukázalo!"
    val newTask = if (lang == Language.EN) "New Task" else "Nový úkol"
    val taskPlaceholder = if (lang == Language.EN) "What to do?" else "Co je třeba udělat?"
    val coinReward = if (lang == Language.EN) "Coin Reward" else "Odměna (mince)"
    val done = if (lang == Language.EN) "Done" else "Hotovo"
    val spinCost = if (lang == Language.EN) "Spin for 20 Coins!" else "Zatoč za 20 mincí!"
    val spinning = if (lang == Language.EN) "Spinning..." else "Točím..."
    val tryAgain = if (lang == Language.EN) "Try again!" else "Zkus to znovu!"
    val jackpot = if (lang == Language.EN) "JACKPOT!" else "JACKPOT!"
    val addRewardsFirst = if (lang == Language.EN) "Add Rewards First" else "Nejdřív přidej odměny"
    val needCoins = if (lang == Language.EN) "Need 20 Coins" else "Potřebuješ 20 mincí"
    val spinBtn = if (lang == Language.EN) "SPIN 20" else "HRÁT 20"
}
