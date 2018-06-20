/**
	Copyright (c) 2008 Victor Stanciu; contact [at] victorstanciu [dot] ro; http://www.victorstanciu.ro/

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
*/

/** 
 * @description		showcase plugin for prototype.js
 * @author		Victor Stanciu; contact [at] victorstanciu [dot] ro; http://www.victorstanciu.ro/ 				
 * @date		03/10/2008
 * @requires		prototype.js 1.6, effects.js 1.8
*/

Showcase = Class.create(Abstract, {
	initialize: function (sections, controls, options) {
	
		this.allSections 	= this.sections = sections;
		this.controls 		= controls;	
		
		this.options = Object.extend({
			ratio: 					0.5,
			initialDelay: 			1,
			duration: 				0.5,			
			size:					this.sections.size()
			}, options || {});
			
		this.running 		= false;
		this.queue			= new Array;
		
		this.computeMetrics();	
		
		this.sections = this.allSections.slice(this.currentIndex - this.half, this.currentIndex + this.half + 1);
		
		this.allSections.each((function (section, index) {
			section.setStyle({position: 'absolute', zIndex: Math.abs(index - this.sections.size()), left: '50%', top: '50%', marginLeft: -Math.round(section.getWidth() / 2) + 'px', marginTop: -Math.round(section.getHeight() / 2) + 'px'}).initialIndex = index;			
			
			section.observe('click', this.jump.bind(this)).observe('mouseover', function (event) {
				section.setOpacity(1);
				}).observe('mouseout', function () {
					section.setOpacity(section.opacity);
					}).opacity = 1;
					
			if (!this.sections.member(section)) {
				this.queue.push(section.hide());
				}
			}).bind(this));
		
		for (i = 0; i <= this.half; i++) {
			this.sections.push(this.sections.shift());
			}
			
		this.controls.invoke('observe', 'click', this.click.bind(this));			
		(this.animate.bind(this)).delay(this.options.initialDelay);
		},

	computeMetrics: function () {
		this.half 			= this.currentIndex = (this.options.size - 1) / 2;		
		this.ratioStep 		= Math.round(((1 - this.options.ratio) / this.currentIndex) * 100) / 100;
		this.positionStep 	= Math.round(50 / this.half * 100) / 100;		
		this.maxDimensions 	= this.sections.first().getDimensions();
		},
		
	click: function (event) {
		event.stop();
		var element = event.findElement('a');
		if (!this.running) {
			eval("this." + element.rel + "()");
			}		
		this.animate(element.rel);
		},
		
	previous: function () {
		if (this.options.size < this.allSections.size()) {
			var sectionIn 	= this.queue.shift();
			var sectionOut 	= this.sections.pop();		
		
			this.sections.unshift(sectionIn);
			this.queue.push(sectionOut.fade({duration: this.options.duration}));
			} else {
				this.sections.unshift(this.sections.pop());
				}
		},
	
	next: function () {		
		if (this.options.size < this.allSections.size()) {			
			var sectionIn 	= this.queue.shift();
			var sectionOut 	= this.sections.shift();
		
			this.sections.push(sectionIn);
			this.queue.push(sectionOut.fade({duration: this.options.duration}));
			} else {
				this.sections.push(this.sections.shift());
				}
		},
	
	jump: function (event) {
		event.stop();
		
		if (!this.running) {
			var section = this.sections[this.sections.indexOf(event.findElement('li'))];
			
			var direction = '';
			
			if (section.index < this.half) {
				(this.half - section.index).times((function () {				
					this.previous();
					}).bind(this));
				direction = 'previous';
				} else if (section.index == this.half) {
					} else {
						(section.index - this.half).times((function () {
							this.next();
							}).bind(this));
						direction = 'next';
						}
			}
		
		this.animate(direction);
		},
	
	runEffects: function () {
		
		this.stackSections.bind(this).delay(this.options.duration / 2);
	
		this.running = new Effect.Parallel(
			this.effects.map(function (effect) {
				return new Effect.Parallel([
					new Effect.Morph(effect.section, {
						style: effect.style,
						sync: true,
						delay: 1,
						transition: Effect.Transitions.linear
						}),
					new Effect.Appear(effect.section, {
						to: Math.min(effect.section.ratio, 1),
						sync: true
						})
					], {
						sync: true,
						beforeStart: function () {							
							}
						});
				}), {
					duration: this.options.duration,
					afterFinish: (function () {
						this.running = false;					
						}).bind(this)
					});
		},
		
	stackSections: function () {
		this.sections.each(function (section) {
			section.setStyle({zIndex: section.stackIndex});
			});
		},
		
	indexSections: function () {	
		this.sections.each((function (section, index) {
			section.index 			= index;
			section.modifier 		= Math.abs(Math.abs((section.index - (this.sections.size() - 1) / 2)) - this.half);
			
			section.ratio 			= Math.round(((section.modifier * this.ratioStep) + this.options.ratio) * 100) / 100;			
						
			section.width 			= Math.min(Math.round(this.maxDimensions.width * section.ratio), this.maxDimensions.width);
			section.height 			= Math.min(Math.round(this.maxDimensions.height * section.ratio), this.maxDimensions.height);		
			
			section.positionIndex 	= (section.index - (this.sections.size() - 1) / 2);
			section.stackIndex 		= Math.abs(Math.abs((section.index - (this.sections.size() - 1) / 2)) - this.half) + 1;					
			
			section.left 			= section.top = Math.round((this.half + section.positionIndex) * this.positionStep);
			section.opacity			= Math.min(section.ratio, 1);			
			}).bind(this));
		}	
});
	
Showcase.Horizontal = Class.create(Showcase, {
	
	animate: function (direction) {
		this.indexSections();
		
		this.effects = new Array();		
		this.sections.each((function (section) {
			var style = {
				left: 		section.left + '%', 
				top:		'50%',
				marginTop:	-Math.abs(section.height / 2) + 'px',
				width: 		section.width + 'px', 
				height: 	section.height + 'px'
				};
			
			if (section.left == 0) {
				style.marginLeft = '0px';
				} else if (section.left == 50) {
					style.marginLeft 		= -Math.round(section.width / 2) + 'px';					
					} else if (section.left == 100) {
						style.marginLeft 	= -section.width + 'px';
						} else {
							style.marginLeft = -Math.round(section.width / 2) + 'px';
							}
			
			this.effects.push({section: section, style: style});		
			}).bind(this));

		this.currentIndex = this.sections[this.half].initialIndex;			
		
		this.runEffects();
		}
	
	});
	
Showcase.Vertical = Class.create(Showcase, {
	
	animate: function (direction) {
		this.indexSections();
		
		this.effects = new Array();		
		this.sections.each((function (section) {
			var style = {
				top: 				section.top + '%', 
				left:				'50%',
				marginLeft:			-Math.abs(section.width / 2) + 'px',				
				width: 				section.width + 'px', 
				height: 			section.height + 'px'				
				};
			
			if (section.top == 0) {
				style.marginTop = '0px';
				} else if (section.top == 50) {
					style.marginTop = -Math.round(section.height / 2) + 'px';					
					} else if (section.top 	== 100) {
						style.marginTop = -section.height + 'px';
						} else {
							style.marginTop = -Math.round(section.height / 2) + 'px';
							}
			
			this.effects.push({section: section, style: style});
			}).bind(this));

		this.currentIndex = this.sections[this.half].initialIndex;			
		
		this.runEffects();
		}
	});
	
Showcase.Diagonal = Class.create(Showcase, {
	
	animate: function (direction) {
		this.indexSections();
		
		this.effects = new Array();		
		this.sections.each((function (section) {
			var style = {
				left: 		section.left + '%', 
				top: 		section.top + '%',				
				width: 		section.width + 'px', 
				height: 	section.height + 'px'
				};
			
			if (section.left == 0) {
				style.marginLeft = '0px';
				} else if (section.left == 50) {
					style.marginLeft 		= -Math.round(section.width / 2) + 'px';					
					} else if (section.left == 100) {
						style.marginLeft 	= -section.width + 'px';
						} else {
							style.marginLeft = -Math.round(section.width / 2) + 'px';
							}
							
			if (section.top == 0) {
				style.marginTop = '0px';
				} else if (section.top == 50) {
					style.marginTop = -Math.round(section.height / 2) + 'px';					
					} else if (section.top 	== 100) {
						style.marginTop = -section.height + 'px';
						} else {
							style.marginTop = -Math.round(section.height / 2) + 'px';
							}							
			
			this.effects.push({section: section, style: style});		
			}).bind(this));

		this.currentIndex = this.sections[this.half].initialIndex;			
		
		this.runEffects();
		}
	
	});