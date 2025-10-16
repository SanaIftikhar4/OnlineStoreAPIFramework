pipeline {
    agent any

    tools {
        jdk 'Java'             // Make sure you have a JDK configured in Jenkins (Manage Jenkins → Global Tool Configuration)
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/SanaIftikhar4/OnlineStoreAPIFramework.git'
            }
        }

        stage('Build & Test') {
            steps {
                //bat './gradlew clean test --no-daemon'
                bat 'gradlew clean test --no-daemon'
            }
        }

        stage('Generate Reports') {
            steps {
            script {
                       // Try to generate Allure reports if configured
                       bat 'gradlew allureReport || echo "Allure task not found, skipping Allure report generation."'
                     }

            }
        }

 stage('Archive Results') { //Archive results for Jenkins artifacts
            steps {
                archiveArtifacts artifacts: 'build/reports/**/*.*', allowEmptyArchive: true
            }
        }


    }


    post {
        always {
            echo 'Pipeline completed.'
        }
    }
}