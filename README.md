# Portfolio Selenium Test Suite

Selenium + TestNG tests for [raphael.tildaitech.co.ke](https://raphael.tildaitech.co.ke).

## Project structure

```
src/test/java/com/portfolio/
├── pages/
│   ├── BasePage.java            # Shared WebDriver helpers (wait, fill, click, JS)
│   ├── AdminLoginPage.java      # Admin login form interactions
│   ├── AdminDashboardPage.java  # Dashboard tabs, modals, forms
│   └── PublicPortfolioPage.java # Public site navigation & contact form
├── tests/
│   ├── BaseTest.java            # ChromeDriver setup/teardown per test
│   ├── LoginTest.java           # Login, logout, invalid credentials
│   ├── ProjectTest.java         # Add project form (textarea, file, checkboxes)
│   ├── CertificateTest.java     # Add certificate form
│   ├── ExperienceTest.java      # Add experience form (dropdown, checkbox)
│   └── PublicSiteTest.java      # Static page, history, contact form, JS scroll
└── utils/
    ├── ConfigReader.java        # Reads config.properties at runtime
    ├── RandomDataGenerator.java # Generates random test data
    └── ScreenshotListener.java  # Auto-screenshots on test failure
```

## Setup

1. **Edit credentials** in `src/test/resources/config.properties`:
   ```properties
   admin.email=admin@email.com
   admin.password=password-here
   ```

2. **Run tests**:
   ```bash
   mvn test
   ```
   **Note:** If maven is not installed, it can be installed by running:
   ```bash
   sudo apt install maven
   ```

3. For **headed mode** (see the browser), set `headless=false` in `config.properties`.

## GitHub Actions

Repository secrets in **Settings → Secrets → Actions**:

| Secret | Value |
|--------|-------|
| `BASE_URL` | `https://raphael.tildaitech.co.ke` |
| `ADMIN_URL` | `https://raphael.tildaitech.co.ke/admin` |
| `ADMIN_EMAIL` | admin email |
| `ADMIN_PASSWORD` | admin password |
