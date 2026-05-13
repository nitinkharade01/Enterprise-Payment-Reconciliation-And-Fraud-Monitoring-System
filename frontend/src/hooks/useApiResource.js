import { useEffect, useState } from 'react';
import { unwrap } from '../api/client.js';

export function useApiResource(loader, fallbackValue) {
  const [data, setData] = useState(fallbackValue);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let active = true;
    loader()
      .then((response) => {
        if (active) {
          setData(unwrap(response));
          setError('');
        }
      })
      .catch(() => {
        if (active) {
          setData(fallbackValue);
          setError('Live service unavailable. Showing workspace sample data.');
        }
      })
      .finally(() => active && setLoading(false));

    return () => {
      active = false;
    };
  }, []);

  return { data, loading, error, setData };
}
