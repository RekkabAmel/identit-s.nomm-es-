# -*- coding: utf-8 -*-
"""
Created on Tue Jun 7 16:23:58 2016

@author: daphnehb
"""

class NotCSVFileException(Exception):
    def __init__(self, value):
        self.value = value

    def __str__(self):
        return repr("Le fichier {} n'est pas un fichier CSV (.csv)".format(self.value))
