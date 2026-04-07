# Riskio 🎰

**Riskio** is a gamified productivity application built with **Jetpack Compose**. It turns your comleted tasks into currency, allowing you to "gamble" your hard-earned coins for real-world rewards that you define.

Whether you're a developer who wants to win "15 minutes of JetBrains research" or just someone trying to make chores more exciting, Riskio brings a bit of high-stakes fun to your To-Do list.

---

## ✨ Features

* **Task-to-Coin Pipeline**: Add tasks. For each task you will gain 10 coins. Complete them to fill your wallet.
* **The Gamba Wheel**: A fully animated three-reel slot machine with two distinct visual modes:
    * **JetBrains Mode**: Spin for your favorite IDE logos (IntelliJ, CLion, Kotlin, etc.).
    * **Classic Mode**: The traditional casino experience with hearts, stars, and lucky symbols.
* **Custom Reward Pool**: You decide what you're playing for. Add custom rewards like "One Episode of Netflix" or "A Fancy Coffee."
* **Price Display**: On screen is presented your own prce to motivate you
* **Localization**: Full support for English (**EN**) and Czech (**CZ**).
* **Dark/Light Mode**: Optimized UI for both late-night coding and daytime productivity.

---

## 🚀 Getting Started

### Prerequisites
* Android Studio (Ladybug or newer)
* Kotlin 2.0+
* Compose Multiplatform / Jetpack Compose environment

### Installation
1.  **Clone the repository**:
    ```bash
    git clone [https://github.com/yourusername/riskio.git](https://github.com/yourusername/riskio.git)
    ```
2.  Open the project in **Android Studio**.
3.  **Sync** the project with Gradle files.
4.  **Run** the app on your emulator or physical device.

---

## 🛠 Tech Stack

* **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative interface.
* **Animations**: `AnimatedContent` and `Coroutine` based delays for smooth slot machine physics.
* **State Management**: `remember`, `mutableStateOf`, and `mutableStateListOf` for reactive UI updates.
* **Icons**: [JetBrains IDE Icons](https://www.jetbrains.com/company/brand/resources/) and Material Design Icons.

---

## 📖 How to Use

1.  **Add Rewards**: Go to the **Home** tab and add rewards you want to win to your pool.
2.  **Earn Coins**: Go to the **Todo** tab, create tasks, and click the "Check" icon once finished to earn coins.
3.  **Spin & Win**: Navigate to the **Gamba** tab. Ensure you have at least **20 coins**. Hit **SPIN**!
1.	4.  **Claim**: If all three reels match, a random reward from your pool is awarded and shown on the **Price Display**.

---

## 🎨 Theme & Customization

Riskio uses **Material 3 (M3)** with a dynamic color scheme. You can toggle between **JetBrains Mode** (Code icon) and **Classic Mode** (Dice icon) in the top bar to change the slot machine's aesthetic without affecting your coins or rewards.

---

## ⚖️ Disclaimer
*This app is intended for productivity and personal entertainment purposes only. No real money is involved, and it does not promote actual gambling.* **Developed at CTU (ČVUT) - FIT.** 🦁

## 👥 Contributors
- [Vojtěch Michal](https://github.com/Vojtech-M)
- [Patrik Pašek](https://github.com/pasekpatrik)
- [Barbora Gregorová](https://github.com/sfmbg)
- [Magdalena Lebedová](https://github.com/majdajede)