init()

async function init(){
    //declare 2 global variable
    username = ""
    orgname = ""
    //show login page
    document.body.className = 'bg-gradient-warning'
    document.title = 'DNET - login'
    var insertPoint = document.getElementById('insertPoint')
    data = {
        TYPE:'get-html',
        FILENAME:'login.html'
    }
    insertPoint.innerHTML = await get(data)
    bindLogin()
}

