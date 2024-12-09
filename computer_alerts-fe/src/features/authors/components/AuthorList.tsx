import { useEffect, useState } from 'react';
import { getAllAuthors } from '../api/getAllAuthors';
import { Button } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './AuthorList.css';

interface Author {
  authorId: string;
  emailAddress: string;
  firstName: string;
  lastName: string;
  biography: string;
}

const AuthorList: React.FC = () => {
  const [authors, setAuthors] = useState<Author[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchAuthors = async () => {
      try {
        const data: Author[] = await getAllAuthors();
        setAuthors(data);
      } catch (err) {
        setError('Error fetching data');
      } finally {
        setLoading(false);
      }
    };

    fetchAuthors();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="author-list">
      {authors.map((author, index) => (
        <div key={author.authorId}>
          <div className="author-header">
            <h1>
              {author.firstName} {author.lastName}
            </h1>
            <i className="bi bi-arrow-90deg-right"></i>
          </div>
          <div className="bio-row">
            <h3>Author BIO: </h3>
            <p>{author.biography}</p>
          </div>
          <h3>Number of Articles Written:</h3>
          <Button id="btn-1">Contact</Button>
          <Button id="btn-2">See More...</Button>
          {index < authors.length - 1 && <hr className="separator" />}
        </div>
      ))}
    </div>
  );
};

export default AuthorList;
