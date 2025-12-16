# Selenium Price Comparison Test

This project is a Selenium WebDriver automation test written in Java.
It compares the prices of a selected book across multiple Turkish e-commerce websites.

## Technologies Used
- Java
- Selenium WebDriver
- TestNG
- WebDriverManager
- ChromeDriver

## Test Scenario
- Search for a book ("Madame Bovary")
- Retrieve the first available price from:
  - N11
  - Kitapyurdu
  - Amazon TR
- Compare:
  - Cheapest price
  - Most expensive price
  - Average price

## Notes
- Amazon may block automated access due to bot protection mechanisms.
- Prices are cleaned and normalized before comparison.
- The test is designed for educational and academic purposes.

## How to Run
1. Clone the repository
2. Open the project in an IDE (IntelliJ IDEA recommended)
3. Run the `ProductCheck` test class
