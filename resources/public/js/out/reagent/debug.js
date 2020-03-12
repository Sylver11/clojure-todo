// Compiled by ClojureScript 1.10.520 {}
goog.provide('reagent.debug');
goog.require('cljs.core');
reagent.debug.has_console = (typeof console !== 'undefined');
reagent.debug.tracking = false;
if((typeof reagent !== 'undefined') && (typeof reagent.debug !== 'undefined') && (typeof reagent.debug.warnings !== 'undefined')){
} else {
reagent.debug.warnings = cljs.core.atom.call(null,null);
}
if((typeof reagent !== 'undefined') && (typeof reagent.debug !== 'undefined') && (typeof reagent.debug.track_console !== 'undefined')){
} else {
reagent.debug.track_console = (function (){var o = ({});
o.warn = ((function (o){
return (function() { 
var G__4954__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"warn","warn",-436710552)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__4954 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__4955__i = 0, G__4955__a = new Array(arguments.length -  0);
while (G__4955__i < G__4955__a.length) {G__4955__a[G__4955__i] = arguments[G__4955__i + 0]; ++G__4955__i;}
  args = new cljs.core.IndexedSeq(G__4955__a,0,null);
} 
return G__4954__delegate.call(this,args);};
G__4954.cljs$lang$maxFixedArity = 0;
G__4954.cljs$lang$applyTo = (function (arglist__4956){
var args = cljs.core.seq(arglist__4956);
return G__4954__delegate(args);
});
G__4954.cljs$core$IFn$_invoke$arity$variadic = G__4954__delegate;
return G__4954;
})()
;})(o))
;

o.error = ((function (o){
return (function() { 
var G__4957__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__4957 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__4958__i = 0, G__4958__a = new Array(arguments.length -  0);
while (G__4958__i < G__4958__a.length) {G__4958__a[G__4958__i] = arguments[G__4958__i + 0]; ++G__4958__i;}
  args = new cljs.core.IndexedSeq(G__4958__a,0,null);
} 
return G__4957__delegate.call(this,args);};
G__4957.cljs$lang$maxFixedArity = 0;
G__4957.cljs$lang$applyTo = (function (arglist__4959){
var args = cljs.core.seq(arglist__4959);
return G__4957__delegate(args);
});
G__4957.cljs$core$IFn$_invoke$arity$variadic = G__4957__delegate;
return G__4957;
})()
;})(o))
;

return o;
})();
}
reagent.debug.track_warnings = (function reagent$debug$track_warnings(f){
reagent.debug.tracking = true;

cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null);

f.call(null);

var warns = cljs.core.deref.call(null,reagent.debug.warnings);
cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null);

reagent.debug.tracking = false;

return warns;
});
