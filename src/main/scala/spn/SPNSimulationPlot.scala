package spn

import de.sciss.chart.api.{XYSeries, XYSeriesCollection}
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.{ChartFactory, ChartPanel}
import u07.modelling.CTMCSimulation.{Event, Trace}
import utils.MSet

import javax.swing.JFrame

object SPNSimulationPlot:

  extension [P](simulation: List[Event[MSet[P]]])
    def plot(): Unit =
      val allPlaces: List[P] = simulation.flatMap(_.state.asList).distinct
      println("All places: " + allPlaces)

      val dataset = new XYSeriesCollection()

      allPlaces foreach { place =>
        val data = new XYSeries(place.toString)
        for event <- simulation
        yield data.add(event.time, event.state.asList.count(_ == place))
        println(data.getItems)
        dataset.addSeries(data)
      }

      val chart = ChartFactory.createXYLineChart(
        "Stochastic Petri Net Simulation",
        "Time",
        "Tokens",
        dataset,
        PlotOrientation.VERTICAL,
        true, // Include legend
        true, // Tooltips
        false // URLs
      )

      val chartPanel = new ChartPanel(chart)

      val frame = new JFrame("")
      frame.setContentPane(chartPanel)
      frame.pack()
      frame.setVisible(true)
