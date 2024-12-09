// src/components/ArticleDetail.tsx
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchArticleByArticleId } from "../api/getSpecificArticle";
import { Article } from "../models/Article";

const ArticleDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>(); // The articleId passed via route
  const [article, setArticle] = useState<Article | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadArticle = async () => {
      try {
        if (id) {
          const data = await fetchArticleByArticleId(id);
          setArticle(data);
        }
      } catch (err) {
        setError("Failed to fetch the article");
      } finally {
        setLoading(false);
      }
    };

    loadArticle();
  }, [id]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return article ? (
    <div>
      <h1>{article.title}</h1>
      <p>
        <strong>Body:</strong> {article.body}
      </p>
      <p>
        <strong>Tags:</strong> {article.tags}
      </p>
      <p>
        <strong>Status:</strong> {article.articleStatus}
      </p>
      <p>
        <strong>Word Count:</strong> {article.wordCount}
      </p>
      <p>
        <strong>Time Posted:</strong>{" "}
        {new Date(article.timePosted).toLocaleString()}
      </p>
    </div>
  ) : (
    <p>No article found.</p>
  );
};

export default ArticleDetails;
