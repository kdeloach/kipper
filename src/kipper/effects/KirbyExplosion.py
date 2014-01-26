from kipper.effects import ParticleEmitterConfig
from kipper.effects.transitions import Linear
from kipper.effects.transitions import EaseInQuad
from kipper.effects.transitions import EaseOutCubic
from kipper.effects.transitions import EaseOutQuad

import math
from random import random

L = EaseOutCubic()

class SampleConfigImpl(ParticleEmitterConfig):

    def getMaxParticles(self):
        return 10

    def getDurationTicks(self):
        return 50

    def getSpawnRate(self):
        return 1

    def isContinuous(self):
        return False

    def isRectShape(self, p):
        return False

    def getSize(self, p):
        return int(L.call(p.ticks, 15, -15, self.getDurationTicks()))

    def getTheta(self, p):
        if p.ticks < 1:
            return random()*2*math.pi
        return p.theta

    def getSpeed(self, p):
        if p.ticks < 1:
            return random()*2
        return p.speed

    def getHue(self, p):
        return 170.0 / 240.0

    def getSaturation(self, p):
        return 110.0 / 240.0

    def getBrightness(self, p):
        return 1
