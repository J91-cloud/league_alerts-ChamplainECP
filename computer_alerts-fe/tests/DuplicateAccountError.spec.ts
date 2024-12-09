import { test, expect } from "@playwright/test";

test("test", async ({ page }) => {
  await page.goto("http://localhost:3000/create-account");
  await page.getByPlaceholder("Enter your email").click();
  await page.getByPlaceholder("Enter your email").fill("user5753@example.com");
  await page.getByPlaceholder("Enter your first name").click();
  await page.getByPlaceholder("Enter your first name").fill("Create");
  await page.getByPlaceholder("Enter your first name").press("Tab");
  await page.getByPlaceholder("Enter your last name").fill("User");
  await page.getByPlaceholder("Enter your password").click();
  await page.getByPlaceholder("Enter your password").fill("@TestPassword1234");
  await page.getByRole("button", { name: "Create User" }).click();
  await expect(page.getByText("Error communicating with")).toBeVisible();
});
