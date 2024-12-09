// src/types/Article.ts
export interface ArticleIdentifier {
  articleId: string;
}

export interface Article {
  id: string;
  articleIdentifier: ArticleIdentifier;
  title: string;
  body: string;
  wordCount: number;
  articleStatus: string;
  tags: string;
  timePosted: string;
}
