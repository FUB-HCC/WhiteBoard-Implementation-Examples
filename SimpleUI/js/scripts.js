// global vars 
var whiteboard = [];
var idCounter = 0;

window.addEventListener('load', loadWhiteboardFromLocalStorage)
window.addEventListener('beforeunload', writeToLocalStorage)

/**
 * @param {string} shapeType 
 * @param {number} id optional parameter
 */

function createShape(shapeType){
    idCounter++
    var shape = {"shapeType":shapeType, "id": idCounter}
    whiteboard.push(shape)
    createShapeElement(shape)
}

function createShapeElement(shape) {
    var div = document.createElement("div")
    div.setAttribute('class', "shape")

    var shapeElement = document.createElement("div")
    shapeElement.setAttribute('class', shape.shapeType)
    shapeElement.setAttribute('id', shape.id)

    var removeButton = document.createElement("button")
    removeButton.setAttribute('id', shape.id)
    removeButton.append("x")
    removeButton.addEventListener('click',deleteShape)

    div.appendChild(shapeElement)
    div.append("id: " +shape.id)
    div.appendChild(removeButton)
    document.getElementById('whiteboard').appendChild(div)
}

function deleteShape(event) {
    var index = whiteboard.findIndex(s => s.id == event.target.id)
    whiteboard.splice(index,1)
    document.getElementById('whiteboard').removeChild(event.target.parentNode)
}
/**
 * localStorage for persistent state
 */
function loadWhiteboardFromLocalStorage(){
    var wbFromStorage = JSON.parse(window.localStorage.getItem("whiteboard"))
    if(Array.isArray(wbFromStorage)){
        whiteboard = wbFromStorage

        if (whiteboard.length>0){ // set global id Counter 
            idCounter = Math.max(...whiteboard.map(s => s.id))
        }
        whiteboard.forEach(shape => {
            createShapeElement(shape)
        });
    }
}
function writeToLocalStorage(){
    window.localStorage.setItem("whiteboard",JSON.stringify(whiteboard))
}
