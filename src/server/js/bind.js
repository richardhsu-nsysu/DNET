function bindLogin(){
  document.getElementById('userLoginBtn').onclick = showUserFrame
  document.getElementById('orgLoginBtn').onclick = showOrgFrame
  document.getElementById('adminLoginBtn').onclick = showAdminFrame
  document.getElementById('goRegister').onclick=showRegisterPage
}

function bindRegister(){
  document.getElementById('userRegister').onclick=showRegisterMsg
  document.getElementById('orgRegister').onclick=showRegisterMsg
  document.getElementById('goOrgRegister').onclick=showOrgRegister
  document.getElementById('goUserRegister').onclick=showUserRegister
  document.getElementById('goLogin').onclick=showLoginPage
}

function bindUserFrame(){
  document.getElementById('sideBtn1').onclick=showUserJoin
  document.getElementById('sideBtn2').onclick=showUserManage
  document.getElementById('logout').onclick=showLoginPage
}

function bindOrgFrame(){
  document.getElementById('sideBtn0').onclick=showOrgAttr
  document.getElementById('sideBtn1').onclick=showOrgPolicy
  document.getElementById('sideBtn2').onclick=showOrgUsers
  document.getElementById('logout').onclick=showLoginPage
}

function bindAdminFrame(){
  document.getElementById('sideBtn1').onclick=showAdminOrgs
  document.getElementById('sideBtn2').onclick=showAdminUsers
  document.getElementById('logout').onclick=showLoginPage
  
}

function bindOrgAttr(){
  document.getElementById('addBtn').onclick = showOrgAttrForm
}

function bindOrgPolciy(){
  document.getElementById('addBtn').onclick = showPolicySubmitMsg
}