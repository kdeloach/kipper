from kipper.effects import ParticleEmitterConfig
from kipper.effects.transitions import Linear
from kipper.effects.transitions import EaseInQuad
from kipper.effects.transitions import EaseOutCubic
from kipper.effects.transitions import EaseOutQuad

import math
from random import random

L = Linear()
E = EaseOutQuad()

class SampleConfigImpl(ParticleEmitterConfig):

    def getMaxParticles(self):
        return 25

    def getDurationTicks(self):
        return 20

    def getSpawnRate(self):
        return 0.5

    def isContinuous(self):
        return True

    def isRectShape(self, p):
        return True

    def getSize(self, p):
        a = 12
        b = 1
        return int(L.call(p.ticks, a, b-a, self.getDurationTicks()))

    def getTheta(self, p):
        return math.pi

    def getSpeed(self, p):
        return 2

    def getHue(self, p):
        return 210.0/360.0

    def getSaturation(self, p):
        a = 10
        b = 100
        return E.call(p.ticks, a, b-a, self.getDurationTicks())/100

    def getBrightness(self, p):
        return 1

