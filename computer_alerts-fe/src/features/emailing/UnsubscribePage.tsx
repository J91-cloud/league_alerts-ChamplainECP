import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';

function UnsubscribePage() {
  const [searchParams] = useSearchParams();
  const [status, setStatus] = useState('LOADING'); // 'LOADING' | 'SUCCESS' | 'ERROR'

  useEffect(() => {
    const email = searchParams.get('email');
    const category = searchParams.get('category');

    if (!email || !category) {
      setStatus('ERROR');
      return;
    }

    // Call your Spring Boot backend
    fetch(`https://dolphin-app-sxvxi.ondigitalocean.app/api/v1/subscriptions/unsubscribe?email=${email}&category=${category}`)
      .then(response => {
        if (response.ok) {
          setStatus('SUCCESS');
        } else {
          setStatus('ERROR');
        }
      })
      .catch(() => setStatus('ERROR'));
  }, [searchParams]);

  if (status === 'LOADING') {
    return (
      <div style={containerStyle}>
        <p>Unsubscribing...</p>
      </div>
    );
  }

  if (status === 'SUCCESS') {
    return (
      <div style={containerStyle}>
        <div style={contentStyle}>
          <div style={greenCircle}>
            <span style={checkMark}>✔</span>
          </div>
          <h1 style={messageStyle}>
            You have successfully unsubscribed from notifications.
          </h1>
        </div>
      </div>
    );
  }

  return (
    <div style={containerStyle}>
      <p style={errorStyle}>
        Oops! Something went wrong while unsubscribing.
      </p>
    </div>
  );
}

const containerStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  minHeight: '100vh',
  textAlign: 'center',
};

const contentStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
};

const greenCircle: React.CSSProperties = {
  width: '150px',
  height: '150px',
  borderRadius: '50%',
  backgroundColor: '#28a745',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
};

const checkMark: React.CSSProperties = {
  fontSize: '80px',
  color: '#fff',
  lineHeight: '1',
};

const messageStyle: React.CSSProperties = {
  fontSize: '24px',
  marginTop: '20px',
  color: '#333',
};

const errorStyle: React.CSSProperties = {
  color: 'red',
  fontSize: '20px',
};

export default UnsubscribePage;
