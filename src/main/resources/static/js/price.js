function price() {
    const start = document.getElementById("start_price").value;
    const end = document.getElementById("end_price").value;

    const startPrice = 'startPrice';
    const endPrice = 'endPrice';

    let ifStart = false;
    let ifEnd = false;

    let url = window.location.href;

    var separator = url.indexOf('?') !== -1 ? "&" : "?";

    var kvp = document.location.search.substr(1).split('&');
    let i=0;

    for(i; i<kvp.length; i++){
        if (kvp[i].startsWith(startPrice + '=') && !isNaN(start) && start!="" && isNormalInteger(start)) {
            let pair = kvp[i].split('=');
            pair[1] = start;
            kvp[i] = pair.join('=');
            ifStart = true;
        } else if (kvp[i].startsWith(startPrice + '=') && start=="") {
            ifStart = true;
        }

        if (kvp[i].startsWith(endPrice + '=') && !isNaN(end) && end!="" && isNormalInteger(end)) {
            let pair = kvp[i].split('=');
            pair[1] = end;
            kvp[i] = pair.join('=');
            ifEnd = true;
        } else if (kvp[i].startsWith(endPrice + '=') && end=="") {
            ifEnd = true;
        }

    }

    var params = kvp.join('&')
    console.log(params);

    if (ifStart == false && !isNaN(start) && isNormalInteger(start)) {
        params = params.concat(separator + startPrice + "=" + start);
        separator = '&'
    }

    if (ifEnd == false && !isNaN(end) && isNormalInteger(end)) {
       params = params.concat(separator + endPrice + "=" + end);
    }

    document.location.search = params;
}

function isNormalInteger(str) {
    var n = Math.floor(Number(str));
    return n !== Infinity && String(n) === str && n >= 0;
}

function cleanFilter() {
    // if(document.location.href.indexOf('?') === -1) {
    //     document.getElementById("clear-filter").classList.remove('d-none');
    //     // $("#clear").style.display = "";
    // } else {
    //     document.getElementById("clear-filter").classList.add('d-none');
    //         // $("#clear").style.display = "none";
    // }
    document.getElementById("clear-filter").classList.add('d-none');
}