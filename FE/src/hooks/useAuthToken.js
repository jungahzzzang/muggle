import Cookies from 'js-cookie';

const useAuthToken = () => {
  const saveToken = (token) => {
    Cookies.set('access_token', token, {
      expires: 1 / 24,  // 1 hour
      secure: true,
      sameSite: 'strict',
    });
  };

  const getToken = () => Cookies.get('access_token');

  const removeToken = () => Cookies.remove('access_token');

  return { saveToken, getToken, removeToken };
};

export default useAuthToken;