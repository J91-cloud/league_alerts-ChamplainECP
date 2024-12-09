import { test, expect } from '@playwright/test';

test('Author Page', async ({ page }) => {
  await page.goto('http://localhost:3000/authors');
  await expect(page.getByRole('heading', { name: 'Author BIO:' }).first()).toBeVisible();
  await expect(page.getByRole('heading', { name: 'Number of Articles Written:' }).first()).toBeVisible();
  
  await page.close();
});