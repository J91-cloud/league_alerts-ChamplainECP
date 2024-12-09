import { Author } from '../model/Author';
import axiosInstance from '../model/authorsAxiosInstance';

export const getAllAuthors = async (): Promise<Author[]> => {
  const response = await axiosInstance.get<Author[]>('/authors');
  return response.data;
};
