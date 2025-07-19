pipeline {
    agent any  // Run on any available Jenkins agent

    tools {
        maven 'Maven'   // Maven version configured in Jenkins
        jdk 'JDK17'          // JDK version configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Pull source code from GitHub
                git 'https://github.com/manojbalineni/otp_service'
            }
        }

        stage('Build') {
            steps {
                // Clean and build the project
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Test') {
            steps {
                // Run all tests
                sh 'mvn test'
            }
        }

        stage('Archive') {
            steps {
                // Save the built JAR file for download
                archiveArtifacts 'target/*.jar'
            }
        }
    }
}
