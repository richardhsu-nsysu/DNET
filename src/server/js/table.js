/*
 *  [table title]
 *  [user table body]
 *  [org table body]
 *  [admin table body]
 */


//[table title]
function buildTableTitle(type,title){
  //build table header
  var thead = document.getElementById('thead')
  thead.innerHTML = ""
  var tr = document.createElement('tr')
  for(let i in title){
    let th = document.createElement('th')
    th.innerHTML = title[i]
    if(type=='user') th.classList.add('text-primary')
    if(type=='org') th.classList.add('text-success')
    if(type=='admin') th.classList.add('text-danger')
    tr.appendChild(th)
  }
  thead.appendChild(tr)
  //build table foot
  var tfoot = document.getElementById('tfoot')
  tfoot.innerHTML = ""
  var tr = document.createElement('tr')
  for(let i in title){
    let th = document.createElement('th')
    th.innerHTML = title[i]
    if(type=='user') th.classList.add('text-primary')
    if(type=='org') th.classList.add('text-success')
    if(type=='admin') th.classList.add('text-danger')
    tr.appendChild(th)
  }
  tfoot.appendChild(tr)
}
  
//[user table body]
function buildUserJoinTableBody(data){
  var tbody = document.getElementById('tbody')
  for(let i in data){
    let tr = document.createElement('tr')
    let td = document.createElement('td')
    let icon = document.createElement('i')
    icon.classList.add('fa')
    icon.classList.add('fa-sign-in-alt')
    //icon.innerHTML = "<span> Join</span>"
    icon.style.cursor = 'pointer'
    //icon.lastChild.className = "d-none d-md-inline"
    //var username = document.getElementById('username').innerHTML.substr(10)
    let org = data[i].orgname
    icon.onclick = ()=>{showUserAttrForm(org,username,"join")}
    td.appendChild(icon)
    tr.appendChild(td)
    let td0 = document.createElement('td')
    td0.innerHTML = data[i].orgname
    tr.appendChild(td0)
    let td1 = document.createElement('td')
    td1.innerHTML = data[i].address
    tr.appendChild(td1)
    let td2 = document.createElement('td')
    td2.innerHTML = data[i].email
    tr.appendChild(td2)
    tbody.appendChild(tr)
  }
}

function buildUserMngTableBody(data){
  var tbody = document.getElementById('tbody')
  for(let i in data){
    let tr = document.createElement('tr')
    //key column
    /*
    let td1 = document.createElement('td')
    let icon1 = document.createElement('i')
    icon1.classList.add('fa')
    icon1.classList.add('fa-key')
    icon1.innerHTML = "<span> Show</span>"
    icon1.style.cursor = 'pointer'
    icon1.lastChild.className = "d-none d-md-inline"
    let orgname = data[i][0]
    icon1.onclick = ()=>{showKeyInfo(orgname)}
    td1.appendChild(icon1)
    tr.appendChild(td1)
    */
    //edit column
    let td2 = document.createElement('td')
    let icon2 = document.createElement('i')
    icon2.classList.add('fa')
    icon2.classList.add('fa-edit')
    icon2.innerHTML = "<span> Edit</span>"
    icon2.style.cursor = 'pointer'
    icon2.lastChild.className = "d-none d-md-inline"
    icon2.onclick = ()=>{showUserAttrForm(data[i].orgname,username,"edit")}
    td2.appendChild(icon2)
    tr.appendChild(td2)
    let td3 = document.createElement('td')
    td3.innerHTML = data[i].orgname
    tr.appendChild(td3)
    let td4 = document.createElement('td')
    td4.innerHTML = data[i].address
    tr.appendChild(td4)
    let td5 = document.createElement('td')
    td5.innerHTML = data[i].email
    tr.appendChild(td5)
    tbody.appendChild(tr)
  }
}

//[org table body]
function buildOrgMngTableBody(data){
  var tbody = document.getElementById('tbody')
  for(let i in data){
    let tr = document.createElement('tr')
    //remove icon
    let td2 = document.createElement('td')
    let icon2 = document.createElement('i')
    icon2.classList.add('fa')
    icon2.classList.add('fa-minus-square')
    icon2.innerHTML = "<span> Remove</span>"
    icon2.style.cursor = 'pointer'
    icon2.lastChild.className = "d-none d-md-inline"
    td2.appendChild(icon2)
    tr.appendChild(td2)
    icon2.onclick = async()=>{
      icon2.parentElement.parentElement.remove()
      var name = icon2.parentElement.nextElementSibling.innerHTML
      var orgname = await get({TYPE:'get-org-name'})
      await get({TYPE:'del-org-attr',NAME:name,ORGNAME:orgname});
    }
    //name
    let td3 = document.createElement('td')
    td3.innerHTML = data[i].name
    tr.appendChild(td3)
    //value
    let td5 = document.createElement('td')
    td5.innerHTML = data[i].value
    tr.appendChild(td5)
    tbody.appendChild(tr)
  }
}

function buildOrgPolicyTableBody(data){
  var tbody = document.getElementById('tbody')
  for(let i in data){
    data[i].value.push("unlimit")
    //row
    let tr = document.createElement('tr')
    tbody.appendChild(tr)
    //name
    let td = document.createElement('td')
    td.innerHTML = data[i].name;
    td.id = "name"+i
    tr.appendChild(td)
    //option1
    let td1 = document.createElement('td')
    td1.appendChild(createDropdown(data[i].value,i,0));
    tr.appendChild(td1)
    //option2
    let td2 = document.createElement('td')
    td2.appendChild(createDropdown(data[i].value,i,1));
    tr.appendChild(td2)
    //option3
    let td3 = document.createElement('td')
    td3.appendChild(createDropdown(data[i].value,i,2));
    tr.appendChild(td3)
  }
  function createDropdown(data,i,j){
    //inputGroupt
    let inputGroup = document.createElement('div')
    inputGroup.classList.add('input-group')
    //input
    let input = document.createElement('select')
    input.id = "input"+i+j
    input.className = "custom-select"
    inputGroup.appendChild(input)
    //option
    for(let j in data){
      let option = document.createElement('option')
      option.value = data[j]
      option.innerHTML = data[j]
      input.appendChild(option)
    }
    return inputGroup;
  }
}

function buildOrgUsersTableBody(data){
  var tbody = document.getElementById('tbody')
  for(let i in data){
    let tr = document.createElement('tr')
    //key column
    /*
    let td3 = document.createElement('td')
    let icon3 = document.createElement('i')
    icon3.classList.add('fa')
    icon3.classList.add('fa-key')
    icon3.innerHTML = "<span> Show</span>"
    icon3.style.cursor = 'pointer'
    icon3.lastChild.className = "d-none d-md-inline"
    let orgname = data[i][0]
    icon3.onclick = ()=>{showKeyInfo(orgname)}
    td3.appendChild(icon3)
    tr.appendChild(td3)
    */
    //edit icon
    let td1 = document.createElement('td')
    let icon1 = document.createElement('i')
    icon1.classList.add('fa')
    icon1.classList.add('fa-edit')
    icon1.innerHTML = "<span> Edit</span>"
    icon1.style.cursor = 'pointer'
    icon1.lastChild.className = "d-none d-md-inline"
    icon1.onclick = async()=>{
      let orgname = await get({TYPE:'get-org-name',USERNAME:username})
      let user = data[i].username
      showUserAttrForm(orgname,user,"edit")
    }
    td1.appendChild(icon1)
    tr.appendChild(td1)
    //ban icon
    /*
    let td0 = document.createElement('td')
    let icon0 = document.createElement('i')
    icon0.classList.add('fa')
    if(data[i][1]=="normal"){
      icon0.classList.add('fa-ban')
      icon0.innerHTML = "<span> Ban</span>"
    }else if(data[i][1]=="banned"){
      icon0.classList.add('fa-redo')
      icon0.innerHTML = "<span> Unban</span>"
    }else if(data[i][1]=="join"){
      icon0.classList.add('fa-check')
      icon0.innerHTML = "<span> Accept</span>"
    }
    icon0.style.cursor = 'pointer'
    icon0.lastChild.className = "d-none d-md-inline"
    td0.appendChild(icon0)
    tr.appendChild(td0)
    */
    //remove icon
    let td2 = document.createElement('td')
    let icon2 = document.createElement('i')
    icon2.classList.add('fa')
    icon2.classList.add('fa-user-times')
    icon2.innerHTML = "<span> Remove</span>"
    icon2.style.cursor = 'pointer'
    icon2.lastChild.className = "d-none d-md-inline"
    td2.appendChild(icon2)
    tr.appendChild(td2)
    icon2.onclick = async()=>{
      let user = data[i].username
      let org = await get({TYPE:'get-org-name'})
      let result = await get({TYPE:'del-org-user',USERNAME:user,ORGNAME:org})
      if(result!='ok') console.log(result)
      icon2.parentElement.parentElement.remove()
    }
    //data
    let td3 = document.createElement('td')
    td3.innerHTML = data[i].username
    tr.appendChild(td3)
    let td4 = document.createElement('td')
    td4.innerHTML = data[i].email
    tr.appendChild(td4)
    tbody.appendChild(tr)
  }
}

//[admin table body]
async function buildAdminOrgsTableBody(data){
  var tbody = document.getElementById('tbody')
  for(let i in data){
    let tr = document.createElement('tr')
    //edit icon
    let td3 = document.createElement('td')
    let icon3 = document.createElement('i')
    icon3.classList.add('fa')
    icon3.classList.add('fa-edit')
    icon3.innerHTML = "<span> Edit</span>"
    icon3.style.cursor = 'pointer'
    icon3.lastChild.className = "d-none d-md-inline"
    icon3.onclick = async()=>{
      orgname = data[i][1]
      showAdminMngOrg()
    }
    td3.appendChild(icon3)
    tr.appendChild(td3)
    //ban icon
    /*
    let td0 = document.createElement('td')
    let icon0 = document.createElement('i')
    icon0.classList.add('fa')
    if(data[i][2]=="normal"){
      icon0.classList.add('fa-ban')
      icon0.innerHTML = "<span> Ban</span>"
    }else if(data[i][2]=="banned"){
      icon0.classList.add('fa-redo')
      icon0.innerHTML = "<span> Unban</span>"
    }else if(data[i][2]=="join"){
      icon0.classList.add('fa-check')
      icon0.innerHTML = "<span> Accept</span>"
    }
    icon0.style.cursor = 'pointer'
    icon0.lastChild.className = "d-none d-md-inline"
    td0.appendChild(icon0)
    tr.appendChild(td0)
    */
    //remove icon
    let td2 = document.createElement('td')
    let icon2 = document.createElement('i')
    icon2.classList.add('fa')
    icon2.classList.add('fa-user-times')
    icon2.innerHTML = "<span> Remove</span>"
    icon2.style.cursor = 'pointer'
    icon2.lastChild.className = "d-none d-md-inline"
    td2.appendChild(icon2)
    tr.appendChild(td2)
    icon2.onclick = ()=>{
      icon2.parentElement.parentElement.remove()
    }
    //data
    for(let j=0;j<3;j++){
      let td = document.createElement('td')
      td.innerHTML = data[i][j]
      tr.appendChild(td)
    }
    tbody.appendChild(tr)
  }
}

function buildAdminUsersTableBody(data){
  var tbody = document.getElementById('tbody')
  for(let i in data){
    let tr = document.createElement('tr')
    //edit icon
    let td1 = document.createElement('td')
    let icon1 = document.createElement('i')
    icon1.classList.add('fa')
    icon1.classList.add('fa-edit')
    icon1.innerHTML = "<span> Edit</span>"
    icon1.style.cursor = 'pointer'
    icon1.lastChild.className = "d-none d-md-inline"
    icon1.onclick = showAdminMngUser
    td1.appendChild(icon1)
    tr.appendChild(td1)
    //ban icon
    let td0 = document.createElement('td')
    let icon0 = document.createElement('i')
    icon0.classList.add('fa')
    if(data[i][1]=="normal"){
      icon0.classList.add('fa-ban')
      icon0.innerHTML = "<span> Ban</span>"
    }else if(data[i][1]=="banned"){
      icon0.classList.add('fa-redo')
      icon0.innerHTML = "<span> Unban</span>"
    }else if(data[i][1]=="join"){
      icon0.classList.add('fa-check')
      icon0.innerHTML = "<span> Accept</span>"
    }
    
    icon0.style.cursor = 'pointer'
    icon0.lastChild.className = "d-none d-md-inline"
    td0.appendChild(icon0)
    tr.appendChild(td0)
    //remove icon
    let td2 = document.createElement('td')
    let icon2 = document.createElement('i')
    icon2.classList.add('fa')
    icon2.classList.add('fa-user-times')
    icon2.innerHTML = "<span> Remove</span>"
    icon2.style.cursor = 'pointer'
    icon2.lastChild.className = "d-none d-md-inline"
    td2.appendChild(icon2)
    tr.appendChild(td2)
    icon2.onclick = ()=>{
      icon2.parentElement.parentElement.remove()
    }
    //data
    for(let j=0; j<3; j++){
      let td = document.createElement('td')
      td.innerHTML = data[i][j]
      tr.appendChild(td)
    }
    tbody.appendChild(tr)
  }
}