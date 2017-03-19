/*----GLOBAL VARIABLES----*/
$fn=100;
t=2;
ts=3.5;
tmin=1;
g_static=0.12;
g_smooth=0.2;



screw=3;    //screw diameter
screwl=12;  //screw long length
screws=7;   //screw short length


//SERVOS
ss=2.3;     //servo  screw diameter
snw=5.7;    //servo nub width
snh=3;      //servo nub height

swt=2;      //servo wing thick

sw=15.25;   //servo width
sl=36.3;    //servo length
sbh=22;     //servo base height


sth=8;     //servo top height
sno=12;     //servo nub offset
ssso=3;     //servo screwy screw offset

sp=7.5;     //servo pin diameter
sal=30;     //servo arm length


//POTENTIOMETER
pnh=10;     //potentiometer nub height
pn=6.4;       //potentiometer nub diameter

//FINGERS
fpin=6;     //finger pin diameter
fppin=4;    //finger pusher pin
fl=75;      //finger length
ftr=15;     //fingertip rotation

//WOOD
wt=3.175;   //wood thickness
wtw=2.7;    //wood thickness for wood
wmw=10;     //wood minimum width
wnl=15;     //wood nub length
jpw=20;     //joint pin diameter


/*----GLOBAL VARIABLES----*/
//1 inch = 25.4 mm = 2.54 cm








/*----HAND----*/
thetamf=60;
screwdriver=6;
//dminf=(fpin/2+g_smooth + t)/cos(thetamf/2) + fppin/2 + g_smooth;
dminf=12.5;
dmaxf=dminf/cos(thetamf/2);


dworm=2*dminf*tan(thetamf/2);
thetaph=180/dworm;
wormiw=max(screwdriver,snw)/2 + g_smooth;

fppino = (fpin/2 + g_smooth + t) + (dmaxf-dminf) + g_smooth + (ts*2/pow(2,1/2));
fpr = (screwdriver/2 + 2*t + 2*g_smooth) +tmin;

hback=16.84;
hfront=sl-sno+ssso+screw/2+t;
hh=dworm+snh+t;



module /*FINAL*/ hand() { //x1
    //has a hole for the fingerpusher
    //has holes for the forearm to connect to
    //has holes for the servo to screw into
    //has a hold for the servo itself
    //has 3 finger holders
        //has a hole for the fingerpin to fit into
    
    up = hh + sth + dmaxf + dmaxf/2;
    side = dminf + fppino;
    
    
    
    difference() {
        //the whole thing
        union() {
            hull() {
                linear_extrude(height=hh+sth)
                hull() {
                    for(i=[0,120,240]) {
                        rotate([0,0,i])
                            ysquare([
                                2*(fpr+g_smooth+t),
                                2*(2*ts+g_smooth)
                            ]);
                    }
                }
                ycube([sl-sno+ssso+screw/2+t,sw+2*t,sth+t]);
            }
            
            //the arms
            for(i=[0,120,240]) {
                rotate([0,0,i]) {
                    difference() {
                        hull() {
                            translate([0,ts+g_smooth,0])
                                cube([
                                    2*(fpr+g_smooth+t),
                                    ts,
                                    0.0001
                                ]);
                            translate([side,ts+g_smooth,up])
                                rotate([-90,0,0])
                                    cylinder(ts,fpin/2+t+g_smooth,fpin/2+t+g_smooth);
                        
                        }
                        
                        //fingerpin hole
                        translate([side,ts+g_smooth,up])
                            rotate([-90,0,0])
                                cylinder(ts,fpin/2+g_static,fpin/2+g_static);
                    }
                    
                    
                    difference() {
                        hull() {
                            translate([0,-g_smooth-2*ts,0])
                                cube([
                                    2*(fpr+g_smooth+t),
                                    ts,
                                    0.0001
                                ]);
                            translate([side,-g_smooth-2*ts,up])
                                rotate([-90,0,0])
                                    cylinder(ts,fpin/2+t,fpin/2+t);
                        }
                        translate([side,-g_smooth-2*ts,up])
                            rotate([-90,0,0]) {
                                translate([0,0,tmin])
                                cylinder(ts,fpin/2+g_static,fpin/2+g_static);
                                cylinder(tmin,fpin/2+tmin+g_static,fpin/2+tmin+g_static);
                            }
                    
                    }
                }
            }
        }
        
        
        
        
        
        //the triangle hole
        translate([0,0,sth])
        linear_extrude(height=hh+up) {
            hull() {
                for(i=[0,120,240]) {
                    rotate([0,0,i])
                        translate([0,-ts-g_smooth,0])
                            square([2*(fpr+g_smooth),ts+g_smooth]);
                }
            }
        }
        
        //the servo hole
        translate([-sno,0,0]) {
            ycube([sl,sw,sth]);
            translate([-ssso,sw/4,0])
                cylinder(screwl-swt,screw/2,screw/2);
            translate([-ssso,sw/-4,0])
                cylinder(screwl-swt,screw/2,screw/2);
            translate([sl+ssso,sw/4,0])
                cylinder(screwl-swt,screw/2,screw/2);
            translate([sl+ssso,sw/-4,0])
                cylinder(screwl-swt,screw/2,screw/2);
        }
        
        
        //the forarm hole
        translate([0,sw/2+ts,0]) {
            translate([-hback,0,0])
                cube([hfront+hback,wt,sth-t]);
            hull() {
                translate([-hback,0,0])
                    cube([5,wt,sth-t]);
                translate([-hback-ts,0,sth+hh])
                    cube([ts,wt,0.0001]);
            }
            translate([9,0,0])
                cube([hfront-9,wt,sth+hh]);
        }
        translate([0,-(sw/2+ts)-wt,0]) {
            translate([-hback,0,0])
                cube([hfront+hback,wt,sth-t]);
            hull() {
                translate([-hback,0,0])
                    cube([5,wt,sth-t]);
                translate([-hback-ts,0,sth+hh])
                    cube([ts,wt,0.0001]);
            }
            translate([9,0,0])
                cube([hfront-9,wt,sth+hh]);
        }
    }
}
module /*FINAL*/ finger() { //x3
    //how far up/down/in the fingertip is
    //should account for the length of the pushey-nub + whatever gap the pusher needs
    do = 20;
    
    mo = sqrt(pow(fl,2)+pow(do,2))/cos(atan(do/fl))/2;
    linear_extrude(height=2*ts) {
        difference() {
            union() {
                hull() {
                    circle(fpin/2 + g_smooth + t);
                    translate([mo,0,0])
                        circle(fpin/2 + g_smooth + t);
                }
                hull() {
                    translate([mo,0,0])
                        circle(fpin/2 + g_smooth + t);
                    translate([fl,do,0])
                        circle(fpin/2 + g_smooth + t);
                }
            }
            circle(fpin/2 + g_smooth);
            translate([fl,do,0])
                rotate([0,0,-ftr])
                    xsquare([
                //the 10 is random, I just dont want to do math
                        fpin + 2*(g_smooth + t) + 10,
                        fpin/2 + g_smooth + t
                    ]);
        }
    }
    
    linear_extrude(height=ts) {
        rotate([0,0,thetamf/2])
        difference() {
            hull() {
                circle(fpin/2 + g_smooth + t);
                translate([0,dmaxf+g_smooth,0])
                    circle(fpin/2 + g_smooth + t);
            }
            hull() {
                translate([0,dminf-g_smooth,0])
                    circle(fppin/2 + g_smooth);
                translate([0,dmaxf+g_smooth,0])
                    circle(fppin/2 + g_smooth);
            }
            circle(fpin/2 + g_smooth);
        }
            
    }
}
module /*FINAL*/ fingerpin() { //x3
    //h is just long enough to stick through both the fingerholderguys
    h=2*g_smooth + 4*ts;
    cylinder(tmin, fpin/2 + tmin, fpin/2 + tmin);
    translate([0,0,0])
        cylinder(h,fpin/2,fpin/2);
}
module /*FINAL*/ fingerpusher() { //x1
    difference() {
        linear_extrude(height=dworm+snh+t) {
            hull() {
                triangle(fpr);
                for(i=[0,120,240]) {
                    rotate([0,0,i])
                        translate([0,-ts,0])
                            square([2*fpr,ts]);
                }
            }
        }
        wormgearcutout();
    }
    
    translate([0,0,dworm+snh+t])
    for(i=[0,120,240]) {
    rotate([90,0,i]) {
        translate([fppino,dmaxf,-ts])
            cylinder(ts,fppin/2,fppin/2);
    
        hull() {
            translate([fppino,dmaxf,0])
                cylinder(ts,fppin/2 +t, fppin/2 +t);
            
            
            translate([fpr*cos(-120),0,0])
                cube([fpr*(2-cos(120)),0.001,ts]);
        }
    }
    }
}
module /*FINAL*/ wormgear() { //x1
    height = dworm + snh;
    
    linear_extrude(twist=thetaph*height,height=height) {
        difference() {
            union() {
                circle(wormiw + t);
                intersection() {
                    union() {
                        xysquare([2*(wormiw + 2*t),wormiw+t]);
                        xysquare([wormiw+t,2*(wormiw + 2*t)]);
                    }
                    circle(screwdriver/2 + 2*t + g_smooth);
                    echo(screwdriver/2 + 2*t + g_smooth);
                }
            }
            circle(screwdriver/2 + g_smooth);
        }
    }
    translate([0,0,snh])
    linear_extrude(height = tmin) {
        difference() {
            circle(screwdriver/2 + g_smooth);
            circle(ss/2);
        }
    }
    difference() {
        cylinder(snh,screwdriver/2+g_smooth,screwdriver/2+g_smooth);
        servonub();
    }
}
module /*FINAL*/ wormgearcutout() {
    height = dworm + snh;
    sidegap = 3*g_smooth;
    linear_extrude(twist=thetaph*height,height=height) {
        circle(wormiw + t + g_smooth);
        intersection() {
            union() {
                xysquare([2*(wormiw + 2*t + g_smooth),wormiw+t+ 2*sidegap]);
                xysquare([wormiw+t+ 2*sidegap,2*(wormiw + 2*t + g_smooth)]);
            }
            circle(screwdriver/2 + 2*t + 2*g_smooth);
        }
    }
}
/*----HAND----*/








/*----ARMS----*/
fal = 150; //forearm length
bcl = 175; //bicep length


module /*FINAL*/ foremain() { //w x2
    fitwidtht=6;
    fitwidthb=hfront-4;
    fitwidthbh=12.5;
    slopiness=0.2;
    
    spfo=72.4;
    
    difference() {
        union() {
            hull() {
                circle(jpw/2 +g_smooth+wmw);
                translate([-hback-ts,sth+hh+fal,0])
                    square([ts+hback+9+fitwidtht,0.0001]);
                translate([fitwidthb,fal,0])
                    square([0.0001,fitwidthbh]);
            }
            hull() {
                circle(jpw/2 +g_smooth+wmw);
                rotate([0,0,-45])
                    translate([0,-spfo,0])
                        circle(sp/2 +g_smooth+wmw);
            }
        }
        
        translate([0,fal+0.0006,0]) {
            difference() {
                translate([-hback,0,0])
                    square([hback+9,sth+hh]);
                
                
                hull() {
                    translate([-hback,0,0])
                        square([5,sth-t]);
                    translate([-hback-ts,sth+hh,0])
                        square([ts+slopiness,0.0001]);
                }
            }
        }
        
        circle(jpw/2 + g_smooth);
        rotate([0,0,-45])
        translate([0,-spfo,0])
            circle(sp/2 + g_static);
        
        for(i=[40,80,120,-40])
            rotate([0,0,(i<0)?-45:0])
            translate([0,i,0])
                crosscutout();
        
    }
}
module /*FINAL*/ bicepmain() { //w x2
    //determing this with le fancy desmos based on distance from the servo to the sevo the the shoulder
    spfo = 140;
    
    difference() {
        hull() {
            circle(jpw/2 +g_smooth+wmw);
            translate([0,bcl,0])
                circle(jpw/2 +g_smooth+wmw);
        }
        
        circle(jpw/2 +g_smooth);
        translate([0,bcl,0])
            circle(jpw/2 +g_smooth);
        
        translate([0,spfo,0])
            circle(sp/2 +g_static);
        
        
        for(i=[50,100,155])
            translate([0,i,0])
                crosscutout();
        
    }
}
module /*FINAL*/ crossbeam(l) { //w x4
    xysquare([wnl,l+2*wt]);
    xysquare([wnl+2*t,l]);
}
module /*FINAL*/ crosscutout() {
    xysquare([wnl,wtw]);
}

/*----ARMS----*/







/*----SWIVEL BASE----*/
armwidth = jpw + 2*g_smooth+ 2*wmw;
twh = 13;   //twisty bit height
twor = 80;  //twisty bit outer radius
twwp = 3.5; //twisty bit wheel pin

teeth = 40;
tooth = 2*PI*(twor-3*ts-g_smooth) / teeth / 2;

dc = 37.5;
dcn = 12;
dcnh = 6;
dcno = 7;
dca = 6;
dca2 = 5.5;
dcal = 25;


gearteeth = 8;
gearradius = tooth * gearteeth / PI;

module baseflat() { //w x1
    
    
    difference() {
        xysquare([2*twor,2*twor]);
        
        //potentio cradle
        //electricity hole
        
        //holes for the sides
        
        rotate([0,0,45])
        for(i=[0,90,180,270]) {
            rotate([0,0,i])
                translate([twor - ts - g_smooth - 2*t,0,0])
                    circle(screw/2);
        }
    }
}
module baseside1() { //w x2
    //has a bunch of nubs to fit into the flat
    //has a bunch of holes for the other sides
}
module baseside2() { //w x2
    //has a bunch of nubs to fit into the flat
    //has a bunch of nubs to fit into the other sides
}
module /*FINAL*/ innerguide() { //x1
    difference() {
        linear_extrude(height = twh) {
            difference() {
                circle(twor - ts - g_smooth);
                
                circle(twor - 3*ts - g_smooth);
                rotate([0,0,360/teeth/2])
                
                //teeth additions
                for(i=[0:360/teeth:360]) {
                    rotate([0,0,i])
                        translate([twor-3*ts-g_smooth,0,0])
                            circle(tooth/2 - g_smooth);
                }
                
                
                //screw holes
                for(i=[0,90,180,270]) {
                    rotate([0,0,i])
                        translate([twor - ts - g_smooth - 2*t,0,0])
                            circle(screw/2);
                }
            }
            
            //teeth bites
            for(i=[0:360/teeth:360]) {
                rotate([0,0,i])
                    translate([twor-3*ts-g_smooth,0,0])
                        circle(tooth/2);
            }
        }
        linear_extrude(height = tmin + g_static) {
            difference() {
                circle(twor - ts - g_smooth);
                circle(twor - ts - g_smooth-tmin);
            }
        }
        
        
        for(i=[0,90,180,270]) {
            rotate([0,0,i])
                translate([twor - ts - g_smooth - 2*t,0,0])
                    cylinder(screwl,screw/2,screw/2);
        }
    }
}
module /*FINAL*/ gear() {
    //has teeth that fit the outerguide
    //has a hole through the middle that fits a DC motor axel
    //has a nub on top
        //has a hole on the side for a screw
    n = gearteeth;
    
    radius = gearradius;
    
    height = twh - 2*t - screw - 2*g_smooth;
    
    linear_extrude(height = height) {
        difference() {
            union() {
                circle(radius);
                for(i=[0:360/n:360]) {
                    rotate([0,0,i])
                        translate([radius,0,0])
                            circle(tooth/2 - g_smooth);
                }
            }
            
            rotate([0,0,360/2/n])
            for(i=[0:360/n:360]) {
                rotate([0,0,i])
                    translate([radius,0,0])
                        circle(tooth/2);
            }
            
            difference() {
                circle(dca/2 + g_static);
                translate([dca2 - dca/2 + g_static,0,0])
                    ysquare([dca - dca2,dca]);
            }
        }
    }
    
    translate([0,0,height]) 
        difference() {
            cylinder(screw + 2*t, dca + ts, dca + ts);
            
            
            linear_extrude(height = screw + 2*t) {
                difference() {
                    circle(dca/2 + g_static);
                    translate([dca2 - dca/2 + g_static,0,0])
                        ysquare([dca - dca2,dca]);
                }
            }
                
            translate([0,0,screw/2 + t/2])
                rotate([0,90,0])
                    #cylinder(dca + ts, screw/2, screw/2);
            
            translate([dca2 - dca/2 + screws -g_smooth,0,0])
                #ycube([5,screw + 1,screw + 2*t]);
        }
}
/*----SWIVEL BASE----*/


gear();





/*----SWIVEL TOP----*/
module /*FINAL*/ topflat() { //w x1
    difference() {
        union() {
            circle(twor);
            
            
            //spot for the screwy screws
            rotate([0,0,45])
            for(i=[0,90,180,270]) {
                rotate([0,0,i]) {
                    ysquare([twor-ts + 2*t + wt,twh+4*t+ 2*screw]);
                }
            }
        }
        
        //screwy screws
        rotate([0,0,45])
        for(i=[0,90,180,270]) {
            rotate([0,0,i]) {
                translate([twor-ts + screw/2 + t,twh/2 +t+ screw/2,0])
                    circle(screw/2);
                translate([twor-ts + screw/2 + t,-(twh/2 +t+ screw/2),0])
                    circle(screw/2);
            }
        }
        
        shoulderbracketcutout();
        
        //elbow servo
        translate([0,(sw/2 + ts + 2*wt + t) + (snh + tmin + 1.5 + wt + t) + sth + g_smooth,0])
            servobracketcutout();
        translate([sp/2 + t -30,(sw/2 + ts + 2*wt + t) + g_smooth,0])
            square([30,snh + tmin + 1.5 + wt + t]);
        
        //shoulder servo
        translate([-((armwidth/2) + sl-sno + ssso*2 + t),-((sw/2 + ts + 2*wt) - sth - (snh + tmin + 1.5)),0])
            servobracketcutout();
        
        
        
        //wire-y hole
        rotate([0,0,-55])
            translate([0,55,0])
                circle(7);
        
        
        
        //dcmotor hole
        translate([0,-((twor - 3*ts - 2*g_smooth) - (gearradius)),0])
            rotate([0,0,-90])
                dcbracketcutout();
        
    }
    
    
}
module /*FINAL*/ outerguide() { //x1
    difference() {
        linear_extrude(height=twh) {
            difference() {
                union() {
                    circle(twor);
                    
                    //spot for screwy screws and wheel house
                    for(i=[0,90,180,270]) {
                        rotate([0,0,i]) {
                            ysquare([twor-ts + 2*t + wt,twh+4*t+ 2*screw]);
                        }
                    }
                }
                circle(twor-ts);
                
                //screwy screws
                for(i=[0,90,180,270]) {
                    rotate([0,0,i]) {
                        translate([twor-ts + screw/2 + t,twh/2 +t+ screw/2,0])
                            circle(screw/2);
                        translate([twor-ts + screw/2 + t,-(twh/2 +t+ screw/2),0])
                            circle(screw/2);
                    }
                }
            }
        }
        
        
        for(i=[0,90,180,270]) {
            rotate([0,0,i])
                translate([twor-t,0,twh/2 - t])
                    rotate([0,90,0]) {
                        cylinder(wt+g_static,twh/2 + g_static,twh/2 + g_static);
                        ycube([twh/2 + g_static,twh + 2*g_static,wt + g_static]);
                        translate([0,0,-t])
                            #cylinder(-ts + 4*t + wt,twwp/2 + g_static,twwp/2 + g_static);
                        translate([0,0,-ts + 3*t + wt - tmin])
                            cylinder(tmin, twwp/2 + tmin + g_static, twwp/2 + tmin + g_static);
                    }
        }
    
    
    }
    
    linear_extrude(height = t) {
        difference() {
            circle(twor - ts);
            circle(twor - ts - t);
        }
    }
}
module /*FINAL*/ wheel() { //w x4
    difference() {
        circle(twh/2);
        circle(twwp/2 + g_static);
    }
}
module /*FINAL*/ wheelpin() {
    cylinder(-ts + 4*t + wt - g_smooth,twwp/2,twwp/2);
    cylinder(tmin, twwp/2 + tmin, twwp/2 + tmin);
    
}
module /*FINAL*/ dcbracket() { //x1
    gap = dcal - wt - twh;
    overlap = 10;
    
    translate([-dcno,0,0])
    difference() {
        cylinder(gap + dcnh + overlap, dc/2 + ts, dc/2 +ts);
        
        translate([dcno,0,0])
            cylinder(gap + dcnh, dcn/2 + g_static, dcn/2 + g_static);
        
        translate([0,0,gap + dcnh])
            cylinder(overlap, dc/2 + g_static, dc/2 + g_static);
        
        rotate([0,0,45])
        for(i=[0,90,180,270]) {
            rotate([0,0,i])
                translate([dc/2,0,0])
                    cylinder(screws, screw/2, screw/2);
        }
    }
}
module /*FINAL*/ dcbracketcutout() {
    circle(dcn/2 + g_static);
    
    translate([-dcno,0,0])
    rotate([0,0,45])
    for(i=[0,90,180,270]) {
        rotate([0,0,i])
            translate([dc/2,0,0])
                circle(screw/2);
    }
    
}
module /*FINAL*/ shoulderbracket() { //x1
    
    difference() {
        union() {
            xycube([armwidth,sw + 2*ts + 4*wt,2*ts]);
            
            //arm 1
            translate([0,sw/2 + ts,0])
            difference() {
                hull() {
                    xcube([jpw + 2*g_smooth+ 2*wmw, 2*wt, ts]);
                translate([0,0,armwidth/2 + 2*ts])
                    rotate([-90,0,0])
                        cylinder(2*wt, armwidth/2,armwidth/2);
                }
            
                translate([0,wt,armwidth/2 + 2*ts])
                    rotate([-90,0,0])
                        cylinder(wt, armwidth/2,armwidth/2);
                translate([0,wt,2*ts])
                    cube([armwidth/2,wt,armwidth/2]);
            }
            
            //arm 2
            translate([0,-sw/2 - ts - 2*wt,0])
            difference() {
                hull() {
                    xcube([jpw + 2*g_smooth+ 2*wmw, 2*wt, ts]);
                    translate([0,0,armwidth/2 + 2*ts])
                        rotate([-90,0,0])
                            cylinder(2*wt, armwidth/2,armwidth/2);
                }
                
                translate([0,0,armwidth/2 + 2*ts])
                    rotate([-90,0,0])
                        cylinder(wt, armwidth/2,armwidth/2);
                translate([0,0,2*ts])
                    cube([armwidth/2,wt,armwidth/2]);
            }
        }
        
        //screw holes
        xoffset = armwidth/2 - ts - screw/2;
        yoffset = sw/2 + 2*wt - screw/2;
        
        translate([xoffset,yoffset,0])
            cylinder(screws,screw/2,screw/2);
        translate([xoffset,-yoffset,0])
            cylinder(screws,screw/2,screw/2);
        translate([-xoffset,yoffset,0])
            cylinder(screws,screw/2,screw/2);
        translate([-xoffset,-yoffset,0])
            cylinder(screws,screw/2,screw/2);
        
        potentiocutout();
        
    }
    
    
    //joint pins
    translate([0,-sw/2 - ts - 2*wt - t/2,armwidth/2 + 2*ts])
        rotate([-90,0,0])
            difference() {
                cylinder(2*wt + t/2, jpw/2 + g_smooth, jpw/2 + g_smooth);
                cylinder(1.5*t,5,5);
            }
    
    translate([0,sw/2 + ts,armwidth/2 + 2*ts])
        rotate([-90,0,0])
            difference() {
                cylinder(2*wt + t/2, jpw/2 + g_smooth, jpw/2 + g_smooth);
                translate([0,0,2*wt - t])
                    cylinder(1.5*t,5,5);
            }
    
    
    
    //offest bicep arm: sw/2 + ts + wt
}
module /*FINAL*/ shoulderbracketcutout() {
    xoffset = armwidth/2 - ts - screw/2;
    yoffset = sw/2 + 2*wt - screw/2;
    
    translate([xoffset,yoffset,0])
        circle(screw/2);
    translate([xoffset,-yoffset,0])
        circle(screw/2);
    translate([-xoffset,yoffset,0])
        circle(screw/2);
    translate([-xoffset,-yoffset,0])
        circle(screw/2);
    
    potentiocutoutflat();
}
module /*FINAL*/ jointpin() { //x4
    difference() {
        union() {
            cylinder(t,jpw/2 +t,jpw/2 +t);
            cylinder(1.5*t + 2*wt,jpw/2,jpw/2);
        }
        translate([0,0,2*wt])
            cylinder(1.5*t,5,5);
    }
}
module /*FINAL*/ jointpintop() { //x4
    difference() {
        cylinder(t,jpw/2 +t,jpw/2 +t);
        translate([0,0,t/2])
            cylinder(t/2,jpw/2 +g_static,jpw/2 +g_static);
    }
    
    cylinder(2*t, 5-g_static,5-g_static);
}
module /*FINAL*/ servoarm1() { //x2
    linear_extrude(height=snh) {
        difference() {
            hull() {
                circle(snw/2 +t);
                translate([sal,0,0])
                    circle(snw/2 +t);
            }
            f_servonub();
        }
    }
    translate([0,0,snh])
    linear_extrude(height=tmin) {
        difference() {
            hull() {
                circle(snw/2 +t);
                translate([sal,0,0])
                    circle(snw/2 +t);
            }
            circle(ss/2);
        }
    }
    translate([0,0,snh+tmin])
    linear_extrude(height=1.5) {
        difference() {
            hull() {
                circle(snw/2 +t);
                translate([sal,0,0])
                    circle(snw/2 +t);
            }
            circle(6.5/2);
        }
    }
    
    translate([sal,0,snh + tmin + 1.5])
        pin(sp, wt);
}
module /*FINAL*/ servoarm2() { //w x1
    l=217.5;
    
    difference() {
        hull() {
            circle(sp/2 +g_static+ts);
            translate([0,l,0])
                circle(sp/2 +g_static+ts);
        }
        circle(sp/2 +g_static);
        translate([0,l,0])
            circle(sp/2 +g_static);
    }
}
module /*FINAL*/ servoarm3() { //w x1
    //length of arm 3
    l=162.5;
    
    difference() {
        hull() {
            circle(sp/2 +g_static+ts);
            translate([0,l,0])
                circle(sp/2 +g_static+ts);
        }
        circle(sp/2 +g_static);
        translate([0,l,0])
            circle(sp/2 +g_static);
    }
}
module /*FINAL*/ servopin(l) { //x2
    
    rotate([180,0,0])
        pin(sp, wt);
    
    cylinder(l,sp/2 +t, sp/2 +t);
    
    translate([0,0,l])
        pin(sp, wt);
}
module /*FINAL*/ servobracket() { //x2
    heightoffset = armwidth/2 + 2*ts;
    
    translate([0,-t-sl+sno,0])
    difference() {
        hull() {
            cube([sbh+ t, sl+ 2*t, heightoffset + sw/2]);
            
            //thing for the servo screwy screw holes
            translate([0,-ssso*2 + t,heightoffset - sw/2])
                cube([screwl,sl + ssso*4,sw]);
        }
        
        
        //sservo hole
        translate([0,t,heightoffset - sw/2])
            cube([sbh,sl,sw]);
        
        //servo wing hole
        translate([0,-ssso*2 + t,heightoffset - sw/2])
            cube([swt,sl + ssso*4,sw]);
        
        //servo wires hole
        translate([sbh-4,sl + t,heightoffset - sw/2])
            cube([4,ssso*2,sw]);
        
        //Servo screwy screw holes
        translate([0,-ssso + t,heightoffset - sw/4])
            rotate([0,90,0])
                cylinder(screwl,screw/2 + 0.2,screw/2 + 0.2);
        translate([0,-ssso + t,heightoffset + sw/4])
            rotate([0,90,0])
                cylinder(screwl,screw/2 + 0.2,screw/2 + 0.2);
        translate([0,sl+ssso + t,heightoffset - sw/4])
            rotate([0,90,0])
                cylinder(screwl,screw/2 + 0.2,screw/2 + 0.2);
        translate([0,sl+ssso + t,heightoffset + sw/4])
            rotate([0,90,0])
                cylinder(screwl,screw/2 + 0.2,screw/2 + 0.2);
        
        
        //screw holes
        translate([screw/2 + t,screw/2 + t,0])
            cylinder(screwl,screw/2,screw/2);
        translate([screw/2 + t,sl + t - screw/2,0])
            cylinder(screwl,screw/2,screw/2);
        translate([sbh - screw/2,screw/2 + t,0])
            cylinder(screwl,screw/2,screw/2);
        translate([sbh - screw/2,sl + t - screw/2,0])
            cylinder(screwl,screw/2,screw/2);
    }
}
module /*FINAL*/ servobracketcutout() {
    rotate([0,0,90])
    translate([0,-t-sl+sno,0]) {
        translate([screw/2 + t,screw/2 + t,0])
            circle(screw/2);
        translate([screw/2 + t,sl + t - screw/2,0])
            circle(screw/2);
        translate([sbh - screw/2,screw/2 + t,0])
            circle(screw/2);
        translate([sbh - screw/2,sl + t - screw/2,0])
            circle(screw/2);
    }
}

module /*FINAL*/ pin(d,l) {
    difference() {
        cylinder(t/2 + l,d/2,d/2);
        cylinder(t/2 + l,d/2 - t,d/2 - t);
    }
}
module /*FINAL*/ pintop(d,l) {
    difference() {
        cylinder(1.5*t,d/2 + t, d/2 + t);
        translate([0,0,t])
            cylinder(t/2, d/2 + g_static, d/2 + g_static);
    }
    cylinder(1.5*t + l, d/2 - t - g_static, d/2 - t - g_smooth);
}
/*----SWIVEL TOP----*/















//pintop(sp,wt) x6
//servopin(?)   x1
//servopin(??)  x1

//hand          x1
//finger        x3
//fingerpin     x3
//fingerpusher  x1
//wormgear();   //x1

//foremain();   //x2 (w)
//bicepmain();  //x2 (w)
//crossbeam(23);    //x4 (w)
//crossbeam(sw + 2*ts + 2*wt);  //x4 (w)

//baseflat      x1 (w)
//baseside1     x2 (w)
//baseside2     x2 (w)
//outerguide    x1
//gear          x1

//topflat       x1 (w)
//innerguide    x1
//wheel         x4 (w)
//wheelpin      x4
//dcbracket     x1
//jointpin      x4
//jointpintop   x4
//servoarm1     x2
//servoarm2     x1 (w)
//servoarm3     x1 (w)
//servopin      x2
//servobracket  x2
//shoulderbracket   x1





/*----UTILITIES----*/
module potentioholder() {
    n=18;
    nl=0.4;
    
    b=(pn/2) - (pn/2)*cos(90/n);
    linear_extrude(height = pnh) {
        difference() {
            circle(pn/2+tmin);
            
            circle(pn/2 -nl);
            for (i=[0:n]) {
                rotate([0,0,(360/n)*i]) {
                    translate([pn/2 -nl-b,0,0])
                        ysquare([nl+b,PI*pn/(2*n)]);
                }
            }
        }
    }
    xycube([pn,0.8,pnh]);
    
    linear_extrude(height=t) {
        difference() {
            union() {
                circle(pn/2+tmin+t);
            }
            circle(pn/2);
        }
    }
}
module potentiocutout() {
    linear_extrude(height=pnh)
    potentiocutoutflat();
}
module potentiocutoutflat() {
    n=18;
    nl=0.4;
    
    b=(pn/2) - (pn/2)*cos(90/n);
    
    difference() {
        union() {
            circle(pn/2 -nl);
            for (i=[0:n]) {
                rotate([0,0,(360/n)*i]) {
                    translate([pn/2 -nl-b,0,0])
                        ysquare([nl+b,PI*pn/(2*n)]);
                }
            }
        }
        xysquare([pn,0.8,]);
    }
}
module triangle(r) {
    d=r*2;
    polygon([
        [d,0],
        [d*cos(120),d*sin(120)],
        [d*cos(-120),d*sin(-120)]
    ]);
}
module f_servonub() {
    n=24;
    nl=0.4;
    
    b=(snw/2) - (snw/2)*cos(90/n);
    
    circle(snw/2 -nl);
    for (i=[0:n]) {
        rotate([0,0,(360/n)*i]) {
            translate([snw/2 -nl-b,0,0])
                ysquare([nl+b,PI*snw/(2*n)]);
        }
    }
}
module fi_servonub() {
    difference() {
        circle(snw/2 +tmin);
        f_servonub();
    }
}
module servonub() {
    linear_extrude(height = snh) {
        f_servonub();
    }
}
module i_servonub() {
    linear_extrude(height=snh) {
        fi_servonub();
    }
}
module t_servonub() {
    width = 20;
    
    translate([0,0,tmin])
        linear_extrude(height = snh) {
            difference() {
                circle(width/2);
                f_servonub();
            }
        }
    linear_extrude(height=tmin) {
        difference() {
            circle(width/2);
            circle(ss/2);
        }
    }
}
module xyzcube(val) {translate([val[0]/-2,val[1]/-2,val[2]/-2]) cube([val[0],val[1],val[2]]);}
module xycube(val) {translate([val[0]/-2,val[1]/-2,0]) cube([val[0],val[1],val[2]]);}
module yzcube(val) {translate([0,val[1]/-2,val[2]/-2]) cube([val[0],val[1],val[2]]);}
module xzcube(val) {translate([val[0]/-2,0,val[2]/-2]) cube([val[0],val[1],val[2]]);}
module xcube(val) {translate([val[0]/-2,0,0]) cube([val[0],val[1],val[2]]);}
module ycube(val) {translate([0,val[1]/-2,0]) cube([val[0],val[1],val[2]]);}
module zcube(val) {translate([0,0,val[2]/-2]) cube([val[0],val[1],val[2]]);}

module xysquare(val) {translate([val[0]/-2,val[1]/-2,0]) square([val[0],val[1]]);}
module xsquare(val) {translate([val[0]/-2,0,0]) square([val[0],val[1]]);}
module ysquare(val) {translate([0,val[1]/-2,0]) square([val[0],val[1]]);}
/*----UTILITIES----*/