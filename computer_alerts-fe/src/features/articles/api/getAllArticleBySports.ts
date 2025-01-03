import axiosInstance from "../models/articlesAxiosInstance";
import { ArticleRequestModel } from "../models/ArticleRequestModel";

export const fetchArticleByTag = async (
  tagName: string,
): Promise<ArticleRequestModel[]> => {
  try {
    const response = await axiosInstance.get<ArticleRequestModel[]>(
      `/articles/tag/${tagName}`,
    );
    return response.data;
  } catch (err) {
    console.error("error fetching articles by tag", err);
    throw err;
  }
};
