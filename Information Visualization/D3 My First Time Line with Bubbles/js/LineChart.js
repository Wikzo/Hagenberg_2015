var lineChart =
{
    createChart:function()
    {
        //console.log("createChart test");

        // SVG
        var height = 400; // scaling unit
        var width = 600; // scaling unit
        var margin = {top: 20, right: 20, bottom: 30, left: 50};

        //https://github.com/mbostock/d3/wiki/Time-Formatting
        // %b - abbreviated month name
        var parseDate = d3.time.format("%d-%b-%y").parse;

        // axis scales
        var x = d3.time.scale().range([0,width]);
        var y = d3.scale.linear().range([height,0]);

        // binding data to axis
        var xAxis = d3.svg.axis().scale(x).orient("bottom");
        var yAxis = d3.svg.axis().scale(y).orient("left");

        // draw the line
        var line = d3.svg.line().x(function(d)
        {
            return x(d.date); // return each date element for each x note
        }).y(function(d)
        {
            return y(d.amount);
        }).interpolate("monotone");

        // SVG (Scalable Vector Graphics) - select via DOM or tag name
        var svg = d3.select("body").append("svg").
            attr("width", width + margin.left + margin.right).
            attr("height", height + margin.top + margin.bottom)
            .append("g").attr("transform", "translate(" + margin.left + ", " + margin.top + ")");

        // get all data
        d3.json("data/data.json", function(error, data) {
            if (error) throw error;

            data.forEach(function (d) // d = each data node
            {
                d.date = parseDate(d.date);
                d.amount = +d.value; // not necessary [can have any name]
                console.log(d);
            });

            // show all dates on the X axis
            x.domain(d3.extent(data, function (d) {
                return d.date;
            }));

            // set Y range
            y.domain([0, d3.max(data, function (d) {
                return d.amount;
            })]);

            // x axis show data
            svg.append("g").attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis);

            // y axis show data
            svg.append("g").attr("class", "y axis")
                .call(yAxis);

            svg.append("path").datum(data)
                .attr("class", "line")
                .attr("d", line);

            // bubbles with radius amount
            // circle SVG attributes: http://www.w3schools.com/svg/svg_circle.asp
            // https://developer.mozilla.org/en-US/docs/Web/SVG/Element/circle

            svg.selectAll("datamarker").data(data).enter()
                .append("circle").attr("class", "datamarker")
                .attr("r", function(d)
                {
                    return d.amount * 0.15; // radius
                })
                .attr("cx", function(d)
                {
                    return x(d.date); // offset by Y (from up top)
                })
                .attr("cy", function(d)
                {
                    return y(d.amount); // offset by Y
                })
                .on("mouseover", function(d) // ease in
                {
                    d3.select(this).transition().ease("elastic")
                        .style("fill-opacity", 1.0).attr("r", function(d)
                        {
                            return d.amount * 0.15 + 15;
                        });

                    //console.log(d);
                })
                .on("mouseout", function(d) // ease out
                {
                    d3.select(this).transition().ease("elastic")
                        .style("fill-opacity", 0.5).attr("r", function(d)
                        {
                            return d.amount * 0.15;
                        });
                });
        });
    }
}