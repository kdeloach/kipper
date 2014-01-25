from kipper.effects import ParticleEmitterConfig
from kipper.effects.transitions import Linear
from kipper.effects.transitions import EaseInQuad
from kipper.effects.transitions import EaseOutCubic
from kipper.effects.transitions import EaseOutQuad

import math
from random import random

L = Linear()
E = EaseOutCubic()

class SampleConfigImpl(ParticleEmitterConfig):

    def getMaxParticles(self):
        return 120

    def getDurationTicks(self):
        return 100

    def getSpawnRate(self):
        return 0.75

    def isContinuous(self):
        return True

    def isRectShape(self, p):
        return False

    def getSize(self, p):
        return int(L.call(p.ticks, 30, -30, self.getDurationTicks()))

    def getTheta(self, p):
        if p.ticks < 1:
            return random()*2*math.pi
        return p.theta

    def getSpeed(self, p):
        if p.ticks < 1:
            return random()*3
        return p.speed

    def getHue(self, p):
        return E.call(p.ticks, 0,360, self.getDurationTicks())/360

    def getSaturation(self, p):
        return 1

    def getBrightness(self, p):
        return E.call(p.ticks, 90,0, self.getDurationTicks())/100

