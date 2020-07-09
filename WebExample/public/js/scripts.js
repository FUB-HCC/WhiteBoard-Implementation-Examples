
// global vars 
const shapeSize = 100

const socket = io()

let userName = "";

const newUserConnected = (user) => {
  userName = user || `User${Math.floor(Math.random() * 1000000)}`;
  socket.emit("new user", userName);
  addToUsersBox(userName);
};

const addToUsersBox = (userName) => {
  if (!!document.querySelector(`.${userName}-userlist`)) {
    return;
  }
  const userBox = `
    <div class="${userName}-userlist">
      <h5>${userName}</h5>
    </div>
  `;
  document.querySelector(".activeUsers").innerHTML += userBox;
};

socket.on("whiteboard", function (whiteboard) {
    whiteboard.map( shape => createShapeElement(shape))
})

socket.on("new user", function (data) {
  data.map((user) => addToUsersBox(user));
});

socket.on("user disconnected", function (userName) {
  document.querySelector(`.${userName}-userlist`).remove();
});

socket.on("create shape", function (shape) {
    createShapeElement(shape)
})

socket.on("delete shape", function (shapeId) {
    deleteShapeElement(shapeId)
})

socket.on("move shape", function (data) {
    const {id, position} = data
    moveShapeElement(id, position)
})

window.addEventListener('load', loadUserNameFromLocalStorage)
window.addEventListener('beforeunload', writeUserNameToLocalStorage)

function createShape(event){
    selectedType = document.getElementById("shapeType")
    shapeType = selectedType.options[selectedType.selectedIndex].value;
    selectedColor = document.getElementById("shapeColor")
    shapeColor = selectedColor.options[selectedColor.selectedIndex].value;
    var shape = {"shapeType":shapeType, "color": shapeColor, "userName": userName, "position":{"left":shapeSize, "top":shapeSize} }
    socket.emit("create shape", shape)
}

function createShapeElement(shape) {
    var whiteboard = document.getElementById("whiteboard")
    const shapeElement = `
    <div id=${shape.id} class="shape"
        style="left: ${shape.position.left}px; top: ${shape.position.top}px;" >
        <div class="${shape.shapeType} has-background-${shape.color}" onmousedown="handleDragStart(event)" 
        ondragstart="null"></div>
        <a class="delete float" onclick="deleteShape(event)"}></a>
    </div>
    `
    whiteboard.innerHTML += shapeElement
}

function deleteShape(event) {
    let shapeId = event.target.parentNode.id
    socket.emit("delete shape", shapeId)
}

function deleteShapeElement(shapeId) {
    const shapeElement = document.getElementById(`${shapeId}`)
    shapeElement ? document.getElementById('whiteboard').removeChild(shapeElement) : null 
}

function moveShapeElement(shapeId, position) {
    const shapeElement = document.getElementById(`${shapeId}`)
    shapeElement.style.left = position.left + 'px';
    shapeElement.style.top = position.top + 'px';
}

function handleDragStart(event) {
    let whiteboardElement = document.getElementById("whiteboard")
    let { left, top } = whiteboardElement.getBoundingClientRect()
    let shapeElement = event.target.className !== "shape" ? event.target.parentNode : event.target
    let shiftX = event.clientX - shapeElement.getBoundingClientRect().x;
    let shiftY = event.clientY - shapeElement.getBoundingClientRect().y;
    var position = {"left": 0, "top": 0}

    function moveAt(pageX, pageY) {
        position.left = pageX - shiftX - left
        position.top = pageY - shiftY - top
        shapeElement.style.left = position.left + 'px';
        shapeElement.style.top = position.top + 'px';
    }
    
    moveAt(event.pageX, event.pageY);

    function onMouseMove(event) {
        moveAt(event.pageX, event.pageY);
      }

    document.addEventListener('mousemove', onMouseMove);
    
    whiteboardElement.onmouseup = function() {
        socket.emit("move shape", {"id":shapeElement.id, position})
        document.removeEventListener('mousemove', onMouseMove);
        shapeElement.onmouseup = null;
      };
}
/**
 * localStorage to remember users
 */
function loadUserNameFromLocalStorage(){
    userName = JSON.parse(window.localStorage.getItem("username"))
    // new user is created so we generate nickname and emit event
    newUserConnected(userName)
}
function writeUserNameToLocalStorage(){
    window.localStorage.setItem("username",JSON.stringify(userName))
}
