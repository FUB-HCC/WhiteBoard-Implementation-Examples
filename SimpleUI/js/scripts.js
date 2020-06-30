function createShape(shapeType) {
    var div = document.createElement("div")
    div.setAttribute('class', shapeType + " shape")
    div.addEventListener('click',deleteShape)
    document.getElementById('whiteboard').appendChild(div)
}

function deleteShape(event) {
    document.getElementById('whiteboard').removeChild(event.target)
}