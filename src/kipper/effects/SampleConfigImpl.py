from kipper.effects import ParticleEmitterConfig
from kipper.effects.transitions import Linear

import math
from random import random

L = Linear()

class SampleConfigImpl(ParticleEmitterConfig):

    def getMaxParticles(self):
        return 48

    def getDurationTicks(self):
        return 45

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
            return random() * 2 * math.pi
        return p.theta

    def getSpeed(self, p):
        if p.ticks < 1:
            return random() * 2
        return p.speed

    def getHue(self, p):
        return 60.0 / 360.0

    def getSaturation(self, p):
        return 1

    def getBrightness(self, p):
        return 1
