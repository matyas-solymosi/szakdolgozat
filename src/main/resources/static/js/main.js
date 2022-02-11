let weights = [];
let ClickType = "INCREASE";
let stepCount = 0;
let startTime = 0;
let gridId;
let algorithmRunning = false;
const url = 'http://localhost:8080';

function generateGrid() {
    $.ajax({
        url: url + "/app/generateGrid",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(33),
        success: function (data) {
            gridId = data.gridId;
            displayResponse(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function resetGrid() {
    $.ajax({
        url: url + "/app/resetGrid",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        async: true,
        data: gridId,
        success: function (data) {
            displayResponse(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function loadGrid() {
    let fileName = document.getElementById('file').files[0].name;
    $.ajax({
        url: url + "/app/loadGrid",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        async: true,
        data: JSON.stringify({
            "gridId": gridId,
            "fileName": fileName
        }),
        success: function (data) {
            displayResponse(data);
        },
        error: function (error) {
            Swal.fire({
                    title: "<h5 style='color:#657fbd'>" + 'An error occurred during the loading.' + "</h5>",
                    background: 'white',
                    confirmButtonColor: '#657fbd',
                }
            )
        }
    });
}

function saveGrid() {
    $.ajax({
        url: url + "/app/saveGrid",
        type: 'POST',
        dataType: "text",
        contentType: "application/json",
        data: gridId,
        success: function (data) {
            var element = document.createElement('a');
            element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(data));
            element.setAttribute('download', 'save.txt');
            element.style.display = 'none';
            document.body.appendChild(element);
            element.click();
            document.body.removeChild(element);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function clickNode(evt) {
    if (!algorithmRunning) {
        let id = evt.target.getAttribute('id');
        $.ajax({
            url: url + "/app/clickNode",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "clickType": ClickType,
                "row": id.substring(5).split("_")[0],
                "column": id.substring(6).split("_")[1],
                "gridId": gridId
            }),
            success: function (data) {
                displayResponse(data);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function generateNoise() {
    $.ajax({
        url: url + "/app/generateNoise",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gridId": gridId,
            "randRange": document.getElementById("randRange").value,
            "startingValue": document.getElementById("startValue").value
        }),
        success: function (data) {
            displayResponse(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function createTable() {

    for (let i = 0; i < 33; i++) {
        let arr = [];
        for (let j = 0; j < 33; j++) {
            arr.push('');
        }
        weights.push(arr);
    }
    let svg = d3.select('#svgOne');
    for (let i = 0; i < 858; i += 26) {
        for (let j = 0; j < 858; j += 26) {
            const id = i / 26 + "_" + j / 26;
            svg.append('rect').attr('width', 26)
                .attr('height', 26)
                .attr('x', i)
                .attr('y', j)
                .style('fill', 'white')
                .attr('stroke', 'black')
                .attr('id', "rect_" + id)
                .attr('class', 'tic')
                .attr('onclick', 'clickNode(evt)');
            svg.append('text').text('')
                .attr('x', i + 26 / 2)
                .attr('y', j + 26 / 2)
                .attr('dominant-baseline', 'middle')
                .attr('text-anchor', 'middle')
                .attr('fill', 'black')
                .attr('id', "text_" + id);
            svg.append('svg:image')
                .attr('x', i)
                .attr('y', j)
                .attr('width', 26)
                .attr('height', 26)
                .attr('id', "imag_" + id)
                .attr('onclick', 'clickNode(evt)');

        }
    }
}

function findPath() {
    $('.control').prop('disabled', true);
    $('#pathLength').text('0');
    $('#algorithmDuration').text('0');
    algorithmRunning = true;
    startTime = new Date().getTime();
    $.ajax({
        url: url + "/app/initializeAlgorithm",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        async: true,
        data: gridId,
        success: function (data) {
            stepCount = 0;
            stepAlgorithm(data.astarId);

        },
        error: function (error) {
            $('.control').prop('disabled', false);
            algorithmRunning = false;
            console.log(error);
        }
    })
}

function stepAlgorithm(aStarId) {
    $.ajax({
        url: url + "/app/stepAlgorithm",
        type: 'POST',
        dataType: "json",
        async: true,
        contentType: "application/json",
        data: aStarId,
        success: function (data) {
            openNodes = data.openNodes;
            $('#stepCount').text(stepCount++);
            displayResponse(data.grid);

            if (data.finished === false) {
                setTimeout(function () {
                    stepAlgorithm(aStarId);
                }, document.getElementById("algorithmSpeed").value * 50);

                for (let i = 0; i < data.openNodes.length; i++) {
                    let id = data.openNodes[i].row + "_" + data.openNodes[i].column;
                    d3.select("#rect_" + id).attr("opacity", "0.3");
                }
                for (let i = 0; i < data.closedNodes.length; i++) {
                    let id = data.closedNodes[i].row + "_" + data.closedNodes[i].column;
                    d3.select("#rect_" + id).attr("opacity", "0.7");
                }
                for (let i = 0; i < data.shortestPath.length; i++) {
                    let id = data.shortestPath[i].row + "_" + data.shortestPath[i].column;
                    if (i === 0) {
                    } else {
                        d3.select("#rect_" + id).style('fill', "violet");
                        d3.select("#rect_" + id).attr("opacity", "1");
                    }
                }
                let id = data.closedNodes[data.closedNodes.length - 1].row + "_" + data.closedNodes[data.closedNodes.length - 1].column;
                d3.select("#rect_" + id).style('fill', "Indigo");
                d3.select("#rect_" + id).attr("opacity", "1");
            } else {
                $('.control').prop('disabled', false);
                $('#pathLength').text(data.shortestPathLength);
                if ((data.shortestPath[0].row !== data.grid.finishNode.row) || (data.shortestPath[0].column !== data.grid.finishNode.column)) {
                    Swal.fire({
                        title: "<h5 style='color:#657fbd'>" + 'No valid path between the start and finish node' + "</h5>",
                        background: 'white',
                        confirmButtonColor: '#657fbd',
                    });
                    displayResponse(data.grid);
                } else {
                    $('#algorithmDuration').text((new Date().getTime() - startTime) % (1000 * 60) + ' ms');
                    for (let i = 0; i < data.shortestPath.length; i++) {
                        let id = data.shortestPath[i].row + "_" + data.shortestPath[i].column;
                        d3.select("#rect_" + id).transition().duration(1000).style('fill', "violet");
                        d3.select("#rect_" + id).attr("opacity", "1");
                    }
                }
                algorithmRunning = false;
            }
        },
        error: function (error) {
            algorithmRunning = false;
            console.log(error);
        }
    });
}

function displayResponse(data) {
    let board = data.nodes;
    for (let i = 0; i < data.size; i++) {
        for (let j = 0; j < data.size; j++) {

            weights[i][j] = board[i][j].height;
            let id = i + "_" + j;

            d3.select("#text_" + id).text(weights[i][j]);
            d3.select("#rect_" + id).attr("opacity", "1");

            let color;
            let link = "";
            if (weights[i][j] < -10) {
                color = "DarkBlue";
            } else if (weights[i][j] < 0) {
                color = "Blue";
            } else if (weights[i][j] < 7) {
                color = "rgb(87, 164, 109)";
            } else if (weights[i][j] < 14) {
                color = "rgb(76, 181, 66)";
            } else if (weights[i][j] < 21) {
                color = "rgb(247, 247, 99)";
            } else if (weights[i][j] < 28) {
                color = "rgb(210, 173, 59)";
            } else if (weights[i][j] < 35) {
                color = "rgb(173, 127, 49)";
            } else if (weights[i][j] < 42) {
                color = "rgb(139, 87, 50)";
            } else if (weights[i][j] < 49) {
                color = "rgb(99, 52, 49)";
            } else {
                color = "White";
            }
            if (data.startNode != null && data.startNode.row === i && data.startNode.column === j) {
                link = "images/start_faded_r.png";
            } else if (data.finishNode != null && data.finishNode.row === i && data.finishNode.column === j) {
                link = "images/finish_faded_r.png";
            } else if (board[i][j].blocked === true) {
                link = 'images/block_faded_r.png';
            }
            d3.select("#rect_" + id).style('fill', color);
            d3.select("#imag_" + id).attr("xlink:href", link);

        }
    }
    if (data.winner != null) {
        alert("Winner is " + data.winner);
    }
}

function setToIncrease() {
    ClickType = 'INCREASE';
    $('#description').text('Increase the value of the clicked cell by one.');
}

function setToDecrease() {
    ClickType = 'DECREASE';
    $('#description').text('Decrease the value of the clicked cell by one.');
}

function setToStart() {
    ClickType = 'SET_START';
    $('#description').text('Set the starting cell of the algorithm.');
}

function setToFinish() {
    ClickType = 'SET_FINISH';
    $('#description').text('Set the finishing cell of the algorithm.');
}

function setToBlock() {
    ClickType = 'BLOCK';
    $('#description').text('Place a block on the clicked cell.');
}

$('input[type=checkbox]').on('click', function (event) {

    if ($('input[type=checkbox]:checked').length === 1) {
        if ($(this).is(':checked')) {
            return false;
        }
        return false;
    } else {

        $('input[type=checkbox]').prop('checked', false);

        $(this).prop('checked', true);
    }


});

document.getElementById("randRange").oninput = function () {
    document.getElementById("range").innerHTML = this.value;
}
document.getElementById("startValue").oninput = function () {
    document.getElementById("value").innerHTML = this.value;
}
