function loginApi(data) {
  return $axios({
    'url': '/api/web/v1/login',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/api/web/v1/logout',
    'method': 'post',
  })
}
