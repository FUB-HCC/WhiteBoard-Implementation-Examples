/***************************************************************************************
* used code example: 
*    Title: Socket.io tutorial: Real-time communication in web development
*    Author: Mateusz Piguła 
*    Date: 30 April, 2020 
*    Availability: https://tsh.io/blog/socket-io-tutorial-real-time-communication/
***************************************************************************************/

const express = require("express")
const socket = require("socket.io")

const PORT = 5000;
const app = express();
const server = app.listen(PORT, function () {
  console.log(`Listening on port ${PORT}`);
  console.log(`http://localhost:${PORT}`);
});

// Static files
app.use(express.static("public"));

// Socket setup
const io = socket(server);

const activeUsers = new Set()
var shapes = []
var idCounter = 0

io.on("connection", function (socket) {
  console.log("Made socket connection");

  socket.on("new user", function (data) {
    socket.userId = data;
    activeUsers.add(data);
    io.emit("new user", [...activeUsers]);

    socket.emit("whiteboard", shapes); // send whiteboard to new user
  });

  socket.on("disconnect", () => {
    activeUsers.delete(socket.userId);
    io.emit("user disconnected", socket.userId);
  });

  socket.on("create shape", function (data) {
    let shape = {...data, id: `shape${idCounter++}`}
    shapes = [...shapes, shape]
    io.emit("create shape", shape)
  })

  socket.on("delete shape", function (shapeId) {
    var index = shapes.findIndex(s => s.id == shapeId)
    if (index!=-1) {
        shapes = [...shapes.slice(0,index), ...shapes.slice(index+1)]
        io.emit("delete shape", shapeId)
    } 
  })

  socket.on("move shape", function (data) {
      const {id, position} = data
      var index = shapes.findIndex(s => s.id == id)
      if (index!==-1) {
        shapes = [...shapes.slice(0,index), {...shapes[index], "position":position}, ...shapes.slice(index+1)]
        io.emit("move shape", data)
      } else {
        socket.emit("refresh")
      }
  })
});
