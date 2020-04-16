/*
 *  outline
 *    [login]
 *    [register]
 * 
 *    [frame]
 *    [user sub frame]
 *    [org sub frame]
 *    [admin sub frame]
 * 
 *    [key info]
 *    [user attr form]
 *    [org attr form]
 */

//ajax promise function
function get(data){
  return new Promise(
      (resolve)=>{
          $.get(
              {
                  url: "Main.php",
                  data:data,
                  success: (result)=>{
                      resolve(result)
                  }
              }
          );
      }
  )
  
}

//[login]
async function showLoginPage(){
  data = {TYPE:'logout'}
  await get(data)
  document.body.className = 'bg-gradient-warning'
  document.title = 'DNET - Login'
  var insertPoint = document.getElementById('insertPoint')
  insertPoint.innerHTML = await get({TYPE:'get-html',FILENAME:'login.html'})
  bindLogin()
}

//[register]
async function showRegisterPage(){
  document.body.className = 'bg-gradient-primary'
  document.title = 'DNET - User Register'
  var insertPoint = document.getElementById('insertPoint')
  insertPoint.innerHTML = await get({TYPE:'get-html',FILENAME:'register.html'})
  bindRegister()
}

function showUserRegister(){
    document.title = 'DNET - User Register'
    document.body.className = 'bg-gradient-primary'
    var elements = document.getElementsByClassName('user-only')
    for(let i=0; i<elements.length; i++){
        elements[i].classList.remove('d-none')
    }
    var elements = document.getElementsByClassName('org-only')
    for(let i=0; i<elements.length; i++){
        elements[i].classList.add('d-none')
    }
}

function showOrgRegister(){
    document.title = 'DNET - Orgnization Register'
    document.body.className = 'bg-gradient-success'
    var elements = document.getElementsByClassName('org-only')
    for(let i=0; i<elements.length; i++){
        elements[i].classList.remove('d-none')
    }
    var elements = document.getElementsByClassName('user-only')
    for(let i=0; i<elements.length; i++){
        elements[i].classList.add('d-none')
    }
}

async function showRegisterMsg(){ 
  if(document.getElementById('address').parentElement.classList.contains('d-none')){
    data = {
      TYPE : 'register',
      GROUP : 'user',
      USERNAME : document.getElementById('username').value,
      EMAIL : document.getElementById('inputEmail').value
    }
  }else{
    data = {
      TYPE : 'register',
      GROUP : 'org',
      USERNAME : document.getElementById('username').value,
      EMAIL : document.getElementById('inputEmail').value,
      ADDRESS : document.getElementById('address').value,
      ORGNAME : document.getElementById('orgName').value
    }
  }
  document.getElementById('notifyMsg').innerHTML=await get(data)
  $('#exampleModalCenter').modal('show')
}

//[frame]
async function showUserFrame(){
  username = document.getElementById('inputUsername').value
  var password = document.getElementById('inputPassword').value
  var data = {
    TYPE : 'login',
    GROUP : 'user',
    USERNAME : username,
    PASSWORD : password
  }
  result = await get(data)
  if(result == 'pass'){
    var insertPoint = document.getElementById('insertPoint')
    insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'frame.html'})
    specialEffect(jQuery)
    document.getElementById('username').innerHTML = 'Welcome ! '+username
    showUserJoin()
    bindUserFrame()
  }else{
    document.getElementById('notifyMsg').innerHTML=result
    $('#exampleModalCenter').modal('show')
  }
}

async function showOrgFrame(){
  username = document.getElementById('inputUsername').value
  var password = document.getElementById('inputPassword').value
  var data = {
    TYPE : 'login',
    GROUP : 'org',
    USERNAME : username,
    PASSWORD : password
  }
  result = await get(data)
  if(result == 'pass'){
    var insertPoint = document.getElementById('insertPoint')
    insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'frame.html'})
    specialEffect(jQuery)
    document.getElementById('username').innerHTML = 'Welcome ! '+username
    document.getElementById('sideBtn0hr').classList.remove("d-none")
    document.getElementById('sideBtn0li').classList.remove("d-none")
    document.getElementById('sideBtn0').lastChild.innerHTML = 'Attribute'
    document.getElementById('sideBtn1').lastChild.innerHTML = 'Polciy'
    document.getElementById('sideBtn2').lastChild.innerHTML = 'Users'
    document.getElementById('colorBtn1').classList.remove('btn-primary')
    document.getElementById('colorBtn1').classList.add('btn-success')
    document.getElementById('colorText1').classList.add('text-success')
    document.getElementById('accordionSidebar').classList.remove('bg-gradient-primary')
    document.getElementById('accordionSidebar').classList.add('bg-gradient-success')
    showOrgAttr()
    bindOrgFrame()
  }else{
    document.getElementById('notifyMsg').innerHTML=result
    $('#exampleModalCenter').modal('show')
  }
  document.getElementById('modalBtn1').classList.remove('btn-primary')
  document.getElementById('modalBtn1').classList.add('btn-success')
}

async function showAdminFrame(){
  username = document.getElementById('inputUsername').value
  var password = document.getElementById('inputPassword').value
  var data = {
    TYPE : 'login',
    GROUP : 'admin',
    USERNAME : username,
    PASSWORD : password
  }
  result = await get(data)
  if(result == 'pass'){
    var insertPoint = document.getElementById('insertPoint')
    insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'frame.html'})
    specialEffect(jQuery)
    document.getElementById('username').innerHTML = 'Welcome ! '+username
    document.getElementById('sideBtn1').lastChild.innerHTML = 'Organizations'
    document.getElementById('sideBtn2').lastChild.innerHTML = 'Users'
    document.getElementById('colorBtn1').classList.remove('btn-primary')
    document.getElementById('colorBtn1').classList.add('btn-danger')
    document.getElementById('colorText1').classList.add('text-danger')
    document.getElementById('accordionSidebar').classList.remove('bg-gradient-primary')
    document.getElementById('accordionSidebar').classList.add('bg-gradient-danger')
    showAdminOrgs()
    bindAdminFrame()
  }else{
    document.getElementById('notifyMsg').innerHTML=result
    $('#exampleModalCenter').modal('show')
  }
}

//[user sub frame]
async function showUserJoin(){
  document.getElementById('sideBtn2').parentNode.classList.remove('active')
  document.getElementById('sideBtn1').parentNode.classList.add('active')
  var insertPoint = document.getElementById('container')
  insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'table.html'})
  document.getElementById('tableTitle').innerHTML = "Join"
  buildTableTitle('user',['Join','Name','Address','Email'])
  var not_join_orgs = await get({TYPE : 'get-notjoin-orgs',USERNAME : username})
  var data
  try { data = JSON.parse(not_join_orgs) }
  catch(e){ console.log(not_join_orgs); return; }
  buildUserJoinTableBody(data)
}

async function showUserManage(){
  document.getElementById('sideBtn1').parentNode.classList.remove('active')
  document.getElementById('sideBtn2').parentNode.classList.add('active')
  var insertPoint = document.getElementById('container')
  insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'table.html'})
  document.getElementById('tableTitle').innerHTML = "Manage"
  buildTableTitle('user',['Edit','Name','Address','Email'])
  var data = await get({TYPE : 'get-join-orgs',USERNAME : username})
  try{data = JSON.parse(data)}
  catch(e){console.log(data);return;}
  buildUserMngTableBody(data)
}

//[org sub frame]
async function showOrgAttr(){
  document.getElementById('sideBtn2').parentNode.classList.remove('active')
  document.getElementById('sideBtn1').parentNode.classList.remove('active')
  document.getElementById('sideBtn0').parentNode.classList.add('active')
  var table = await get({TYPE : 'get-html',FILENAME : 'table.html'});
  //set attribute table
  insertPoint = document.getElementById('container')
  insertPoint.innerHTML = table
  tableTitle = document.getElementById('tableTitle')
  tableTitle.innerHTML = "Attribute"
  tableTitle.classList.remove('text-primary')
  tableTitle.classList.add('text-success')
  buildTableTitle('org',['Remove','Name','Value'])
  var orgname = await get({TYPE : 'get-org-name',USERNAME:username});
  var attr = await get({TYPE : 'get-org-attr',ORGNAME:orgname});
  var data = JSON.parse(attr)
  buildOrgMngTableBody(data)
  var addBtn = document.getElementById('addBtn')
  addBtn.classList.remove('d-none')
  addBtnHR = document.getElementById('addBtnHR')
  addBtnHR.classList.remove('d-none')
  //var editBtn = document.getElementById('editBtn')
  //editBtn.classList.remove('d-none')
  //editBtnHR = document.getElementById('editBtnHR')
  //editBtnHR.classList.remove('d-none')

  orgname = await get({TYPE:'get-org-name',USERNAME:username})
  bindOrgAttr()
}

async function showOrgPolicy(){
  document.getElementById('sideBtn2').parentNode.classList.remove('active')
  document.getElementById('sideBtn1').parentNode.classList.add('active')
  document.getElementById('sideBtn0').parentNode.classList.remove('active')
  var table = await get({TYPE : 'get-html',FILENAME : 'table.html'});
  //set policy table
  var insertPoint = document.getElementById('container')
  insertPoint.innerHTML = table
  var tableTitle = document.getElementById('tableTitle')
  tableTitle.innerHTML = "Policy"
  tableTitle.classList.remove('text-primary')
  tableTitle.classList.add('text-success')
  buildTableTitle('org',['Name','Option1','Option2','Option3'])
  orgname = await get({TYPE :'get-org-name'});
  var data = await get({TYPE :'get-org-attr',ORGNAME :orgname});
  try{data = JSON.parse(data)}
  catch{console.log(data);return;}
  buildOrgPolicyTableBody(data)
  //set submit btn
  var addBtnHR = document.getElementById('addBtnHR')
  addBtnHR.classList.remove('d-none')
  var addBtn = document.getElementById('addBtn')
  addBtn.innerHTML = "Submit"
  addBtn.classList.remove('d-none')
  bindOrgPolciy()
}

async function showPolicySubmitMsg(){
  var data = [];
  for(let i=0;;i++){
    let td = document.getElementById("name"+i)
    if(td==null) break;
    let name = td.innerHTML
    let options = []
    for(let j=0;j<3;j++){
      options.push(document.getElementById("input"+i+j).value)
    }
    data.push({name:name,options:options})
  }
  data = JSON.stringify(data)
  var orgname = await get({TYPE:"get-org-name"})
  var result = await get({TYPE:"set-org-policy",ORGNAME:orgname,USERNAME:username,DATA:data})
  var msg
  if(result == "ok") msg = "Success! Please restart app to get new public key"
  else msg = result
  document.getElementById('modalLabel').innerHTML="Submit Result"
  document.getElementById('modalBody').innerHTML=msg
  document.getElementById('modalBtn1').onclick = ()=>{
    $("#frameModal").modal('hide')
  }
  $("#frameModal").modal('show')
}

async function showOrgUsers(){
  document.getElementById('sideBtn2').parentNode.classList.add('active')
  document.getElementById('sideBtn1').parentNode.classList.remove('active')
  document.getElementById('sideBtn0').parentNode.classList.remove('active')
  var insertPoint = document.getElementById('container')
  insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'table.html'})
  var tableTitle = document.getElementById('tableTitle')
  tableTitle.innerHTML = "Users"
  tableTitle.classList.remove('text-primary')
  tableTitle.classList.add('text-success')
  buildTableTitle('org',['Edit','Remove','Name','E-mail'])
  var orgname = await get({TYPE : 'get-org-name'})
  var data = await get({TYPE : 'get-org-user',ORGNAME:orgname})
  try{data = JSON.parse(data)}
  catch{console.log("showOrgUsers : \n",data);return;}
  buildOrgUsersTableBody(data)
}

//[admin sub frame]
async function showAdminOrgs(){
  document.getElementById('sideBtn2').parentNode.classList.remove('active')
  document.getElementById('sideBtn1').parentNode.classList.add('active')
  var insertPoint = document.getElementById('container')
  insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'table.html'})
  var tableTitle = document.getElementById('tableTitle')
  tableTitle.innerHTML = "Organizations"
  tableTitle.classList.remove('text-primary')
  tableTitle.classList.add('text-danger')
  buildTableTitle('admin',['Edit','Remove','Orgname','Address','Email'])
  var data = await get({TYPE : 'get-orgs'})
  try{data = JSON.parse(data)}
  catch(e){console.log(data);return;}
  buildAdminOrgsTableBody(data)
}

async function showAdminMngOrg(){
  var insertPoint = document.getElementById('container')
  insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'table.html'})
  var tableTitle = document.getElementById('tableTitle')
  tableTitle.innerHTML = "Attributes"
  tableTitle.classList.remove('text-primary')
  tableTitle.classList.add('text-danger')
  var data = JSON.parse(await get({TYPE : 'get-org-attr'}))
  data = [{
    name:'age',
    type:'range',
    min:10,
    max:100
  },{
    name:'department',
    type:'option',
    options:['CSE','EE','EEE']
  },{
    name:'address',
    type:'text'
  },{
    name:'phone',
    type:'number'
  }]
  buildTableTitle('admin',['Remove','Name','Type','Detail'])
  var addBtn = document.getElementById('addBtn')
  addBtn.classList.remove('d-none')
  addBtn.classList.remove('btn-success')
  addBtn.classList.add('btn-danger')
  var addBtnHR = document.getElementById('addBtnHR')
  addBtnHR.classList.remove('d-none')
  var editBtn = document.getElementById('editBtn')
  editBtn.classList.remove('d-none')
  editBtn.classList.remove('btn-success')
  editBtn.classList.add('btn-danger')
  var editBtnHR = document.getElementById('editBtnHR')
  editBtnHR.classList.remove('d-none')
  var returnBtn = document.getElementById('returnBtn')
  returnBtn.classList.remove('d-none')
  returnBtn.onclick = showAdminOrgs
  var returnBtnHR = document.getElementById('returnBtnHR')
  returnBtnHR.classList.remove('d-none')
  buildOrgMngTableBody(data)
  orgname = await get({TYPE:'get-org-name',USERNAME:username})
  bindOrgManage()
}

async function showAdminUsers(){
  document.getElementById('sideBtn1').parentNode.classList.remove('active')
  document.getElementById('sideBtn2').parentNode.classList.add('active')
  var insertPoint = document.getElementById('container')
  insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'table.html'})
  var tableTitle = document.getElementById('tableTitle')
  tableTitle.innerHTML = "Users"
  tableTitle.classList.remove('text-primary')
  tableTitle.classList.add('text-danger')
  buildTableTitle('admin',['Edit','Remove','Name','Email'])
  var data = JSON.parse(await get({TYPE : 'get-org-users'}))
  buildAdminUsersTableBody(data)
}

async function showAdminMngUser(){
  var insertPoint = document.getElementById('container')
  insertPoint.innerHTML = await get({TYPE : 'get-html',FILENAME : 'table.html'})
  var tableTitle = document.getElementById('tableTitle')
  tableTitle.innerHTML = "Manage"
  tableTitle.classList.remove('text-primary')
  tableTitle.classList.add('text-danger')
  var returnBtnHR = document.getElementById('returnBtnHR')
  returnBtnHR.classList.remove('d-none')
  buildTableTitle('admin',['Edit','Name','Address'])
  var returnBtn = document.getElementById('returnBtn')
  returnBtn.classList.remove('d-none')
  returnBtn.onclick = showAdminUsers
  var returnBtnHR = document.getElementById('returnBtnHR')
  returnBtnHR.classList.remove('d-none')
  var data = JSON.parse(await get({TYPE : 'get-join-orgs'}))
  buildUserMngTableBody(data)
}

//[key info]
async function showKeyInfo(orgname){
  //change title
  var title = document.getElementById('modalLabel')
  title.innerHTML = "Keys"
  //get key from ajax
  var key1 = await get({TYPE:"get-key",ORGNAME:orgname})
  var key2 = await get({TYPE:"get-parameter",ORGNAME:orgname})
  //key1 = "parameter error"
  var modalBody = document.getElementById('modalBody')
  $("#frameModal").modal('show')
  if(key1=="parameter error"||key2=="parameter error"){
    modalBody.innerHTML="Parameter error, please modify your arrtibute of the organization and try again !"
    return
  }
  modalBody.innerHTML="<p>private key : "+key1+"</p>"
  var modalBtn1 = document.getElementById('modalBtn1')
  modalBtn1.innerHTML = 'Copy private key'
  modalBtn1.onclick = ()=>{
    var copyArea = document.createElement('input')
    copyArea.type="text"
    copyArea.value=key1
    modalBody.appendChild(copyArea)
    copyArea.select()
    document.execCommand("copy")
    copyArea.remove()
  }
  /*
  var modalBtn2 = document.getElementById('modalBtn2')
  modalBtn2.innerHTML = 'Copy public key'
  modalBtn2.classList.remove("d-none")
  modalBtn2.onclick = ()=>{
    var copyArea = document.createElement('input')
    copyArea.type="text"
    copyArea.value=key2
    modalBody.appendChild(copyArea)
    copyArea.select()
    document.execCommand("copy")
    copyArea.remove()
  }
  */
}

//[user attr form]
async function showUserAttrForm(org,user,mode){
  //generate modal frame
  var table = await get({TYPE:'get-html',FILENAME:'user-attr-table.html'})
  var modalBody = document.getElementById('modalBody')
  if(data!=[]) modalBody.innerHTML = table
  var tbody = document.getElementById('userAttrTbody')
  var title = document.getElementById('modalLabel')
  title.innerHTML = "User atrributes"
  //show user original value table
  if(mode=="edit"){
    var data = await get({TYPE:"get-user-attr",USERNAME:user,ORGNAME:org})
    try{data = JSON.parse(data)}
    catch(e){console.log(data);return;}
    for(let i in data){
      let tr = document.createElement('tr')
      let td1 = document.createElement('td')
      td1.innerHTML = data[i].name
      tr.appendChild(td1)
      let td3 = document.createElement('td')
      td3.innerHTML = data[i].value
      tr.appendChild(td3)
      tbody.appendChild(tr)
    }
  }
  //get attr format
  var format = await get({TYPE:"get-org-attr",ORGNAME:org})
  try{ format = JSON.parse(format) }
  catch(e){ console.log(format);  return; }
  //generate input area by attr format
  for(let i in format){
    //create input group and prepend
    let inputGroup = document.createElement('div')
    inputGroup.classList.add('input-group')
    let inputPrepend = document.createElement('div')
    inputPrepend.classList.add('input-group-prepend')
    let spanPrepend = document.createElement('span')
    spanPrepend.className = "input-group-text"
    spanPrepend.innerHTML = format[i].name
    inputPrepend.appendChild(spanPrepend)
    inputGroup.appendChild(inputPrepend)
    modalBody.appendChild(inputGroup)
    let input = document.createElement('select')
    input.id = "input"+i
    input.className = "custom-select"
    inputGroup.appendChild(input)
    for(let j in format[i].value){
      let option = document.createElement('option')
      option.value = format[i].value[j]
      option.innerHTML = format[i].value[j]
      input.appendChild(option)
    }
    //add hr
    if(i<format.length-1){
      let hr = document.createElement('hr')
      modalBody.appendChild(hr)
    }
  }
  //set submit buttons
  var submit = document.getElementById('modalBtn1')
  submit.innerHTML = 'Submit'
  submit.onclick = async()=>{
    var form = []
    for(var i in format){
      let value = document.getElementById('input'+i).value
      let name = format[i].name
      let attr = {
        orgname:org,
        name:name,
        value:value
      }
      form.push(attr)
    }
    form = JSON.stringify(form)
    var result1 = await get({TYPE:'set-user-attr',DATA:form,USERNAME:username,ORGNAME:org})
    var result2 = await get({TYPE:'user-join-org',USERNAME:username,ORGNAME:org})
    if(result1=='ok'&& result2=='ok'){
      $("#frameModal").modal('hide')
      if(mode=="join") showUserJoin();
    }
    else console.log("result1:",result1,"\nresult2:",result2)
  }
  var modalBtn2 = document.getElementById('modalBtn2')
  modalBtn2.classList.add("d-none")
  $("#frameModal").modal('show')
}

//[org attr form]
async function showOrgAttrForm(){
  //change title
  var title = document.getElementById('modalLabel')
  title.innerHTML = "Orgnaization attribute"
  //change btn
  var btn = document.getElementById('modalBtn1')
  btn.innerHTML = "Submit"
  //insert form html
  var form = await get({TYPE:'get-html',FILENAME:'org-attr-form.html'})
  document.getElementById('modalBody').innerHTML = form
  document.getElementById('orgAttrOptions').classList.remove('d-none')
  document.getElementById('orgAttrOptionsHr').classList.remove('d-none')
  //submit
  document.getElementById('modalBtn1').onclick=async()=>{
    var orgname = await get({TYPE:'get-org-name',USERNAME:username})
    var name = document.getElementById('nameInput').value
    var value = document.getElementById('optionsInput').value
    var response = await get({TYPE:'set-org-attr',ORGNAME:orgname,NAME:name,VALUE:value})
    if(response == 'ok'){
      showOrgAttr();
      $("#frameModal").modal('hide');
    }  
  }
  //show modal
  $("#frameModal").modal('show')

}

//[org account form]
async function showOrgAccountForm(){
  //change title
  var title = document.getElementById('modalLabel')
  title.innerHTML = "Orgnaization information"
  //change btn
  var btn = document.getElementById('modalBtn1')
  btn.innerHTML = "Submit"
  //insert form html
  var form = await get({TYPE:'get-html',FILENAME:'org-info.html'})
  document.getElementById('modalBody').innerHTML = form
  //get info of account
  var orgname = await get({TYPE:'get-org-name',USERNAME:username})
  var data = await get({TYPE:'get-org-info',ORGNAME:orgname})
  data = ['org1','address1']
  //set form default value
  document.getElementById('orgInfoOrgname').value = data[0]
  document.getElementById('orgInfoAddress').value = data[1]
  //set info of account when submit
  //show modal
  $("#frameModal").modal('show')

}


