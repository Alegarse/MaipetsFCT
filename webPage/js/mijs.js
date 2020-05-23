window.onload = function () {

    var s = Snap("#svg");

    Snap.load("icono.svg", function (f) {

        tapa = f.select("#tapa");
        container = f.select("#papelera");
        container.hover(hoverover, hoverout);
        s.append(f);
    });

    var hoverover = function () {
        tapa.animate({transform: "t0,-15"},800,mina.bounce);
        document.getElementById("sonido").play();
    }; //And return to original position when not hovered over

    var hoverout = function () {
        tapa.animate({transform: ""},800,mina.bounce);
    };
};