// https://www.youtube.com/watch?list=PL6gx4Cwl9DGBMdkKFn3HasZnnAqVjzHn_&v=KsjrN-T3ZCs

function placeAnOrder(orderNumber)
{
    console.log("Customer order: " + orderNumber);
    //console.log("Please wait 5 seconds ...");

    cookAndDeliverFood(function()
    {
        console.log("***Delivered food order: " + orderNumber);
    });
}

// simulate a 5-second operation
function cookAndDeliverFood(callback)
{
    setTimeout(callback, 5000); // setting schedule to fire off in 5 seconds
}

// simulate user web requests
placeAnOrder(1);
placeAnOrder(2);
placeAnOrder(3);
placeAnOrder(4);
placeAnOrder(5);