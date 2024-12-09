import { test, expect } from "@playwright/test";

test("test", async ({ page }) => {
  const randomNumber = Math.floor(Math.random() * 100000000);
  const randomEmail = `user${randomNumber}@example.com`;

  await page.goto("http://localhost:3000/create-account");
  await page.getByPlaceholder("Enter your email").click();
  await page.getByPlaceholder("Enter your email").fill(randomEmail);
  await page.getByPlaceholder("Enter your email").press("Tab");
  await page.getByPlaceholder("Enter your first name").fill("Create");
  await page.getByPlaceholder("Enter your first name").press("Tab");
  await page.getByPlaceholder("Enter your last name").fill("User");
  await page.getByPlaceholder("Enter your last name").press("Tab");
  await page.getByPlaceholder("Enter your password").fill("@Testpassword1234");
  await page.getByRole("button", { name: "Create User" }).click();
  await expect(page.getByText("User created successfully! ID")).toBeVisible();
});
